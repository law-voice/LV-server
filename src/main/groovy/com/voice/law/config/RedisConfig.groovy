package com.voice.law.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
 class RedisConfig {
    @Autowired
     void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer()
        redisTemplate.setKeySerializer(stringSerializer)
        redisTemplate.setValueSerializer(stringSerializer)
        redisTemplate.setHashKeySerializer(stringSerializer)
        redisTemplate.setHashValueSerializer(stringSerializer)
    }
}
