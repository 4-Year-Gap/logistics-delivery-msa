package com.springcloud.hub.domain.service;

import com.springcloud.hub.domain.api.NaverMapClient;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.hub.interfaces.NaverMapApiResponse;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.domain.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteDomainService {
    private final HubRouteRepository hubRouteRepository;
    private final NaverMapClient naverMapClient;
    private final ObjectMapper objectMapper;

    /**
     * 네이버 API 호출 및 DB 저장
     */
    public HubRoute fetchAndSaveRoute(Hub startHub, Hub goalHub) {
        String jsonResponse = naverMapClient.requestOptimalRoute(
                startHub.getLatitude(), startHub.getLongitude(),
                goalHub.getLatitude(), goalHub.getLongitude());

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

    /**
     * 모든 허브에서 목적지까지 최적 경로 조회
     */
    public List<HubRoute> getOptimalRoutesForAllHubs(List<Hub> hubs) {
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
}