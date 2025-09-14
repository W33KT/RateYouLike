package com.hmdp.utils;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author tankaiwen
 */
@Component
public class RedisService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public boolean tryLock(String key, Long expireTime, TimeUnit unit) {
        Boolean res = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", expireTime, unit);
        return BooleanUtils.isTrue(res);
    }

    public void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    public void saveWithLogicExpire(String key, Object value, Long expireSec) {
        RedisData redisData = new RedisData()
                .setData(value)
                .setExpireTime(LocalDateTime.now().plusSeconds(expireSec));

        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(redisData));
    }

    public RedisData getData(String key) {
        String json = stringRedisTemplate.opsForValue().get(key);

        return JSON.parseObject(json, RedisData.class);
    }

    public void saveWithExpire(String json, String redisKey, Long expire, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(redisKey, json, expire, timeUnit);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
