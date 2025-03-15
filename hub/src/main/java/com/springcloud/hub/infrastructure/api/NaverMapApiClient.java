package com.springcloud.hub.infrastructure.api;

import com.springcloud.hub.config.NaverMapProperties;
import com.springcloud.hub.domain.api.NaverMapClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class NaverMapApiClient implements NaverMapClient {
    private final WebClient webClient;
    private final NaverMapProperties properties;

    @Override
    public String requestOptimalRoute(BigDecimal startLat, BigDecimal startLon, BigDecimal goalLat, BigDecimal goalLon) {
        String url = String.format("%s?start=%s,%s&goal=%s,%s",
                properties.getUrl(), startLon, startLat, goalLon, goalLat);

        return webClient.get()
                .uri(url)
                .header("x-ncp-apigw-api-key-id", properties.getKeyId())
                .header("x-ncp-apigw-api-key", properties.getKeyValue())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}