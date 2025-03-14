package com.spring_cloud.eureka.client.order.infrastructure.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:properties/env.properties") // env.properties 파일 소스 등록
})
public class PropertyConfig {

}