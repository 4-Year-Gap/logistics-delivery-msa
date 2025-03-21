package com.springcloud.client.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.client.user.infrastructure.IdentityIntegrationDto;
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

//    @Bean
//    public RedisTemplate<String, IdentityIntegrationDto> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, IdentityIntegrationDto> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//
//        // JSON 직렬화 설정
//        Jackson2JsonRedisSerializer<IdentityIntegrationDto> serializer = new Jackson2JsonRedisSerializer<>(IdentityIntegrationDto.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        serializer.setObjectMapper(objectMapper);
//
//        // Key는 String, Value는 JSON 직렬화
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(serializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }

    @Bean
    public RedisTemplate<String, IdentityIntegrationDto> redisTemplate() {
        RedisTemplate<String, IdentityIntegrationDto> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
//        template.setHashValueSerializer(RedisSerializer.json());
        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonRedisSerializer<IdentityIntegrationDto> serializer = new Jackson2JsonRedisSerializer<>(IdentityIntegrationDto.class);

        template.setHashValueSerializer(serializer);

        return template;
    }
}
