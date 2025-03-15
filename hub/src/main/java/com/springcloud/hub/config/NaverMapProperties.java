package com.springcloud.hub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "naver.api")
@Getter
@Setter
public class NaverMapProperties {
    private String url;
    private String keyId;
    private String keyValue;
}