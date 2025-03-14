package com.springcloud.hub.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.hub.application.NaverMapApiResponse;
import com.springcloud.hub.application.RouteResponse;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.infrastructure.HubRepository;
import com.springcloud.hub.infrastructure.HubRouteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NaverMapHubApiService {
    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${naver.api.url}")
    private String naverApiUrl;

    @Value("${naver.api.key.id}")
    private String apiKeyId;

    @Value("${naver.api.key.value}")
    private String apiKey;

    /**
     * 특정 허브에서 목적지까지 최적 경로 조회 (단일 허브)
     */
    public HubRoute getOptimalRoute(UUID startHubId, UUID goalHubId) {
        Optional<Hub> startHubOptional = hubRepository.findById(startHubId);
        if (startHubOptional.isEmpty()) {
            throw new IllegalArgumentException("출발지 허브 정보를 찾을 수 없습니다: " + startHubId);
        }
        Hub startHub = startHubOptional.get();

        Optional<Hub> goalHubOptional = hubRepository.findById(goalHubId);
        if (goalHubOptional.isEmpty()) {
            throw new IllegalArgumentException("도착지 허브 정보를 찾을 수 없습니다: " + goalHubId);
        }
        Hub goalHub = goalHubOptional.get();

        return fetchAndSaveRoute(startHub, goalHub);
    }

    /**
     * 모든 허브에서 목적지까지 경로 조회 (리스트)
     */
    public List<HubRoute> getOptimalRoutesForAllHubs() {
        List<Hub> hubs = hubRepository.findAllHubs();

        List<HubRoute> results = new ArrayList<>();

        for (Hub startHub : hubs) {
            for (Hub goalHub : hubs) {
                if (!startHub.getId().equals(goalHub.getId())) { // 자기 자신 제외
                    HubRoute hubRoute = fetchAndSaveRoute(startHub, goalHub);
                    results.add(hubRoute);
                }
            }
        }
        return results;
    }

    /**
     * 네이버 API 호출 공통 로직
     */
    private String requestOptimalRoute(BigDecimal startLat, BigDecimal startLon, BigDecimal goalLat, BigDecimal goalLon) {
        String url = String.format("%s?start=%s,%s&goal=%s,%s",
                naverApiUrl, startLon, startLat, goalLon, goalLat);

        return webClient.get()
                .uri(url)
                .header("x-ncp-apigw-api-key-id", apiKeyId)
                .header("x-ncp-apigw-api-key", apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 동기 호출 (비동기 처리 시 .subscribe())
    }

    /**
     * 네이버 API 호출 및 DB 저장
     */
    public HubRoute fetchAndSaveRoute(Hub startHub, Hub goalHub) {
        String jsonResponse = requestOptimalRoute(startHub.getLatitude(), startHub.getLongitude(), goalHub.getLatitude(), goalHub.getLongitude());

        try {
            NaverMapApiResponse response = objectMapper.readValue(jsonResponse, NaverMapApiResponse.class);

            if (response.code() == 0 && !response.route().traoptimal().isEmpty()) {
                NaverMapApiResponse.Summary summary = response.route().traoptimal().get(0).summary();

                // 소요시간 (밀리초 -> LocalTime 변환)
                LocalTime timeRequired = LocalTime.ofSecondOfDay(summary.duration() / 1000);

                // 이동거리
                BigDecimal moveDistance = summary.distance();

                // HubRoute 엔티티 생성 및 저장
                HubRoute hubRoute = HubRoute.builder()
                        .Id(UUID.randomUUID())
                        .fromHub(startHub)
                        .toHub(goalHub)
                        .timeRequired(timeRequired)
                        .moveDistance(moveDistance)
                        .isDeleted(false)
                        .build();

                return hubRouteRepository.save(hubRoute);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}