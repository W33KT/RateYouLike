package com.hmdp.utils;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisCommands;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
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

    public void batchAddGeo(String key, List<RedisCommands.GeoLocation<String>> geoLocations) {
        stringRedisTemplate.opsForGeo().add(key, geoLocations);
    }

    public List<GeoResult<RedisGeoCommands.GeoLocation<String>>> queryGeo(String key, Double x, Double y, Double distance, int start, int end) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> result = stringRedisTemplate.opsForGeo()
                .search( // default: sorted by distance
                        key,
                        GeoReference.fromCoordinate(x, y),
                        new Distance(distance),
                        RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(end));
        if (Objects.isNull(result)) {
            return Collections.emptyList();
        }

        return result.getContent().stream().skip( start).toList();
    }
}
