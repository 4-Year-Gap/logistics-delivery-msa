package com.springcloud.hub.infrastructure.api;

import com.springcloud.hub.config.NaverMapProperties;
import com.springcloud.hub.domain.api.NaverMapClient;
import com.springcloud.hub.interfaces.exception.CustomTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

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
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(TimeoutException.class, e -> new CustomTimeoutException("Naver 요청 API 타임아웃"))
                .block();
    }
}