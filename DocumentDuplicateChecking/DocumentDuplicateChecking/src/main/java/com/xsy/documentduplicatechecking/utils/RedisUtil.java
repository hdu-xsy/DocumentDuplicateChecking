package com.xsy.documentduplicatechecking.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author xushiyue
 * @date 2019年7月31日10:04:07
 */
@Component
public class RedisUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void setHour(String key, String value, Long time) {
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.HOURS);
    }

    public void setMinute(String key, String value, Long time) {
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void del(String key) {
        stringRedisTemplate.delete(key);
    }
}