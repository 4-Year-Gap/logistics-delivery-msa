package com.springcloud.hub.infrastructure.external;

import com.springcloud.hub.application.dto.CreateHubCommand;
import com.springcloud.hub.application.dto.FindAddressQuery;
import com.springcloud.hub.interfaces.exception.CustomTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class KakaoMapClientImpl implements KakaoMapClient {
    private final WebClient webClient;

    @Value("${kakao.maps.api.url}")
    private String kakaoMapUrl;

    @Value("${kakao.maps.api.key}")
    private String key;

    @Override
    public String requestGeocoding(FindAddressQuery command, Pageable pageable) {
        String url = String.format("%s?query=%s&page=%d&size=%d",
                kakaoMapUrl,
                command.address(),
                pageable.getPageNumber(),
                pageable.getPageSize());

        return webClient.get()
                .uri(url)
                .header("Authorization", "KakaoAK " + key)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(TimeoutException.class, e -> new CustomTimeoutException("Kakao 요청 API 타임아웃"))
                .block();
    }
}
