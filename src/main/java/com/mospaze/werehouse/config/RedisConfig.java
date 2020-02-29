package com.mospaze.werehouse.config;

import com.mospaze.werehouse.model.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String REDIS_HOST;

    @Value("${spring.redis.port}")
    private String REDIS_PORT;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory
                = new JedisConnectionFactory();
        jedisConFactory.setHostName(REDIS_HOST);
        jedisConFactory.setPort(Integer.parseInt(REDIS_PORT));
        return jedisConFactory;
    }

    @Bean
    RedisTemplate<String, JWT> redisTemplate() {
        RedisTemplate<String, JWT> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }
}
