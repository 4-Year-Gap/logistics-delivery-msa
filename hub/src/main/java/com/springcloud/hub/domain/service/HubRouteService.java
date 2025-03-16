package com.springcloud.hub.domain.service;

import com.springcloud.hub.application.RouteInfo;
import org.springframework.stereotype.Service;

import com.springcloud.hub.infrastructure.external.NaverMapApiResponse;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteService {

    /**
     * 경로 정보를 기반으로 HubRoute 엔티티를 생성
     */
    public HubRoute createHubRoute(Hub startHub, Hub goalHub, RouteInfo routeInfo) {
        return HubRoute.builder()
                .Id(UUID.randomUUID())
                .fromHub(startHub)
                .toHub(goalHub)
                .timeRequired(routeInfo.timeRequired())
                .moveDistance(routeInfo.moveDistance())
                .isDeleted(false)
                .build();
    }

    /**
     * 양방향 HubRoute 생성
     */
    public List<HubRoute> createBidirectionalRoutes(Hub startHub, Hub goalHub, RouteInfo forwardRouteInfo, RouteInfo backwardRouteInfo) {
        HubRoute forwardRoute = createHubRoute(startHub, goalHub, forwardRouteInfo);
        HubRoute backwardRoute = createHubRoute(goalHub, startHub, backwardRouteInfo);
        return Arrays.asList(forwardRoute, backwardRoute);
    }
}