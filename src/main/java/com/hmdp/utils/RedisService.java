package com.hmdp.utils;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author tankaiwen
 */
@Component
public class RedisService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

    public void save(String json, String redisKey, Long expire, TimeUnit timeUnit) {
        if (Objects.nonNull(expire) && Objects.nonNull(timeUnit)) {
            stringRedisTemplate.opsForValue().set(redisKey, json, expire, timeUnit);
            return;
        }
        stringRedisTemplate.opsForValue().set(redisKey, json);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Boolean existInSet( String key, String value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    public void addToSet(String key, String value) {
        stringRedisTemplate.opsForSet().add(key, value);
    }

    public void removeFromSet(String key, String value) {
        stringRedisTemplate.opsForSet().remove(key, value);
    }

    public Set<String> querySet(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    public Set<String> getIntersection(String... keys) {
        return stringRedisTemplate.opsForSet().intersect(Arrays.asList(keys));
    }

    public Boolean existInSortedSet(String key, String value) {
        return Objects.nonNull(stringRedisTemplate.opsForZSet().score(key, value));
    }

    public void addToSortedSet(String key, String value, double score) {
        stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    public void removeFromSortedSet(String key, String value) {
        stringRedisTemplate.opsForZSet().remove(key, value);
    }

    public Set<String> queryFromSortedSet(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<String>> pageQueryFromSortedSet(String key, double min, double max, long offset, long count) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min , max, offset, count);
    }
}
