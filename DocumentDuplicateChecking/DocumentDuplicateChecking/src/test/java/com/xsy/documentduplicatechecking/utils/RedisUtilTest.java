package com.xsy.documentduplicatechecking.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class RedisUtilTest {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void redisTemplateTest() {
        assertNotNull(stringRedisTemplate);
    }

    @Test
    public void setTest() {
        redisUtil.setHour("key", "value", 1L);
        assertEquals("value", redisUtil.get("key"));
    }

    @Test
    public void getTest() {
        String value = redisUtil.get("key");
        assertNull(value);
        //assertEquals("null",value);
    }
}