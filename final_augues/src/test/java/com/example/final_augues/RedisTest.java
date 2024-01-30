package com.example.final_augues;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class RedisTest {
    // Lua脚本
    private final String LUA_SCRIPT = "if redis.call('SETNX', KEYS[1], ARGV[1]) == 1 then\n" +
            "    redis.call('EXPIRE', KEYS[1], ARGV[2])\n" +
            "    return true\n" +
            "else\n" +
            "    return false\n" +
            "end";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void lua() {
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>(LUA_SCRIPT, Boolean.class);
        String token = "abcdefg";
        // 使用Lua脚本执行原子性操作
        Boolean success = redisTemplate.execute(script, Collections.singletonList(token), "true", "600");
        System.out.println(success);
    }

    @Test
    public void storeData() {

        String key = "name";
        String value = "0917ningbin";
        redisTemplate.opsForValue().set(key, value);
    }

    @Test
    public void genCookier() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("userid" + i + ",usertoken" + i);
        }
    }

}
