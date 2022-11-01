package com.itheima.service.mongo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

@Component
public class RedisIdService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Long getNextId(String collectionName) {
        // 创建long类型主键自增器对象
        RedisAtomicLong redisAtomicLong = new RedisAtomicLong("id:" + collectionName, stringRedisTemplate.getConnectionFactory());
        // 自增并返回
        return redisAtomicLong.incrementAndGet();
    }
}
