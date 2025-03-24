package com.springcloud.company.company.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.company.common.IdentityIntegrationResponse;
import com.springcloud.company.company.infrastructure.dto.IdentityIntegrationCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {


    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.username}")
    private String username;

    @Value("${spring.data.redis.password}")
    private String password;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setUsername(username);
        config.setPassword(password);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, IdentityIntegrationCommand> redisTemplate() {
        RedisTemplate<String, IdentityIntegrationCommand> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonRedisSerializer<IdentityIntegrationCommand> serializer = new Jackson2JsonRedisSerializer<>(IdentityIntegrationCommand.class);

        template.setHashValueSerializer(serializer);

        return template;
    }

    @Bean
    public RedisTemplate<String, IdentityIntegrationResponse> resRedisTemplate() {
        RedisTemplate<String, IdentityIntegrationResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonRedisSerializer<IdentityIntegrationResponse> serializer = new Jackson2JsonRedisSerializer<>(IdentityIntegrationResponse.class);

        template.setHashValueSerializer(serializer);

        return template;
    }
}