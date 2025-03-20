package com.springcloud.hub.infrastructure.external;

import com.springcloud.hub.interfaces.exception.CustomTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class NaverMapClientImpl implements NaverMapClient {
    private final WebClient webClient;

    @Value("${naver.maps.api.directions5.url}")
    private String directions5Url;

    @Value("${naver.maps.api.key.id}")
    private String id;

    @Value("${naver.maps.api.key.value}")
    private String value;

    @Override
    public String requestOptimalRoute(BigDecimal startLat, BigDecimal startLon, BigDecimal goalLat, BigDecimal goalLon) {
        String url = String.format("%s?start=%s,%s&goal=%s,%s",
                directions5Url, startLon, startLat, goalLon, goalLat);

        return webClient.get()
                .uri(url)
                .header("x-ncp-apigw-api-key-id", id)
                .header("x-ncp-apigw-api-key", value)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(TimeoutException.class, e -> new CustomTimeoutException("Naver 요청 API 타임아웃"))
                .block();
    }
}