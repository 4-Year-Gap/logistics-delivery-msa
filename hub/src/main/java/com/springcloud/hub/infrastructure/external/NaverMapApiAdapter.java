package com.springcloud.hub.infrastructure.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.hub.application.dto.FindNaverRouteQuery;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.exception.ExternalApiException;
import com.springcloud.hub.interfaces.exception.RouteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class NaverMapApiAdapter {
    private final NaverMapClient naverMapClient;
    private final ObjectMapper objectMapper;

    /**
     * 두 허브 사이의 최적 경로 정보를 네이버 맵 API로부터 조회
     */
    public FindNaverRouteQuery getOptimalRouteInfo(Hub startHub, Hub goalHub) {
        try {
            String jsonResponse = naverMapClient.requestOptimalRoute(
                    startHub.getLatitude(), startHub.getLongitude(),
                    goalHub.getLatitude(), goalHub.getLongitude());

            NaverMapApiResponse response = objectMapper.readValue(jsonResponse, NaverMapApiResponse.class);

            if (response.code() != 0 || response.route().traoptimal().isEmpty()) {
                throw new RouteNotFoundException(startHub.getId(), goalHub.getId());
            }

            NaverMapApiResponse.Summary summary = response.route().traoptimal().get(0).summary();
            return new FindNaverRouteQuery(
                    LocalTime.ofSecondOfDay(summary.duration() / 1000),
                    summary.distance()
            );
        } catch (JsonProcessingException e) {
            throw new ExternalApiException("네이버 맵 API 응답 처리 중 오류가 발생했습니다.");
        }
    }
}