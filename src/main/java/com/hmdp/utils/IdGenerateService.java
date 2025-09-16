package com.hmdp.utils;

import com.hmdp.constants.GlobalConstants;
import com.hmdp.constants.RedisConstants;
import com.hmdp.exception.SystemException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author tankaiwen
 */
@Service
public class IdGenerateService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int BIT_COUNT = 32;
    /**
     * Generate a global unique ID.
     *
     * @param bizKey business key (e.g., "order", "user")
     * @return generated global ID
     */
    public Long generateId(String bizKey) {
        String date = LocalDate.now().format(YYYY_MM_DD);
        String redisKey = bizKey + ":" + date;

        // Increment counter atomically
        Long seq = stringRedisTemplate.opsForValue().increment(redisKey);
        if (Objects.isNull( seq)) {
            throw new SystemException("generate id error!");
        }

        // Set expiration if key is new
        if (GlobalConstants.ONE_LONG.equals( seq)) {
            stringRedisTemplate.expire(redisKey, RedisConstants.GLOBAL_KEY_EXPIRE_DAYS, TimeUnit.DAYS);
        }

        // Generate ID
        long currentSec = System.currentTimeMillis() / 1000;
        return currentSec << BIT_COUNT | seq;
    }
}
