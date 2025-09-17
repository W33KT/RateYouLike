package com.hmdp.utils;

import com.hmdp.constants.LuaConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Redis distributed lock service with:
 * - Reentrant support
 * - Watchdog auto-renewal
 * - Safe unlock using Lua script\
 *
 * @author tankaiwen
 */
@Slf4j
@Service
public class RedisLockService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Schema(description = "Thread-local to store lock value (UUID + thread ID)")
    private final ThreadLocal<String> lockFlag = new ThreadLocal<>();

    @Schema(description = "Thread-local to store reentrant count")
    private final ThreadLocal<Integer> lockCount = ThreadLocal.withInitial(() -> 0);

    @Schema(description = "Thread-local to store watchdog ScheduledFuture")
    private final ThreadLocal<ScheduledFuture<?>> watchdogFuture = new ThreadLocal<>();

    @Schema(description = "Single-threaded scheduled executor for watchdog tasks")
    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    /**
     * Try to acquire lock
     *
     * @param key        lock key
     * @param expireTime lock expiration time
     * @param unit       time unit
     * @return true if lock is acquired
     */
    public boolean tryLock(String key, long expireTime, TimeUnit unit) {
        // Reentrant check: if already locked by current thread, increment count
        if (lockCount.get() > 0) {
            lockCount.set(lockCount.get() + 1);
            return true;
        }

        // Generate unique value for this thread
        String value = UUID.randomUUID() + "-" + Thread.currentThread().getId();
        Boolean res = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, value, expireTime, unit);

        if (BooleanUtils.isFalse(res)) {
            return false;
        }

        lockFlag.set(value);
        lockCount.set(1);

        // Start watchdog auto-renewal
        startWatchdog(key, value, expireTime, unit);

        return true;
    }

    /**
     * Unlock the lock safely
     *
     * @param key lock key
     */
    public void unlock(String key) {
        if (lockCount.get() == 0) {
            // Current thread not hold the lock
            return;
        }

        int count = lockCount.get() - 1;
        lockCount.set(count);

        if (count == 0) {
            // Cancel watchdog
            ScheduledFuture<?> future = watchdogFuture.get();
            if (future != null) {
                future.cancel(true);
                watchdogFuture.remove();
            }

            // Execute Lua script to safely delete the lock
            String value = lockFlag.get();
            stringRedisTemplate.execute(
                    new DefaultRedisScript<>(LuaConstants.UNLOCK_LUA, Long.class),
                    Collections.singletonList(key),
                    value
            );

            lockFlag.remove();
            lockCount.remove();
        }
    }

    /**
     * Start watchdog task to automatically renew lock expiration
     *
     * @param key        lock key
     * @param value      lock value
     * @param expireTime expiration time
     * @param unit       time unit
     */
    private void startWatchdog(String key, String value, long expireTime, TimeUnit unit) {
        // renew every 1/3 of expireTime
        long refreshInterval = unit.toMillis(expireTime) / 3;

        ScheduledFuture<?> future = SCHEDULER.scheduleAtFixedRate(() -> {
            try {
                String currentValue = stringRedisTemplate.opsForValue().get(key);
                if (value.equals(currentValue)) {
                    stringRedisTemplate.expire(key, expireTime, unit);
                }
            } catch (Exception e) {
                log.error("Redis lock watchdog error for key {}: {}", key, e.getMessage());
            }
        }, refreshInterval, refreshInterval, TimeUnit.MILLISECONDS);

        watchdogFuture.set(future);
    }
}

