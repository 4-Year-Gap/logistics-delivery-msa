package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record FindHubRouteQuery(
        UUID id,
        UUID toHubId,
        UUID fromHubId,
        Hub toHub,
        Hub fromHub,
        BigDecimal moveDistance,
        LocalTime timeRequired
) {
    public FindHubRouteQuery(HubRoute route) {
        this(
                route.getId(),
                route.getToHub().getId(),
                route.getFromHub().getId(),
                route.getToHub(),
                route.getFromHub(),
                route.getMoveDistance(),
                route.getTimeRequired()
        );
    }

    // 팩토리 메서드로 새로운 객체 생성
    public static FindHubRouteQuery fromHubRoute(HubRoute route) {
        return new FindHubRouteQuery(
                route.getId(),
                route.getToHub().getId(),
                route.getFromHub().getId(),
                route.getToHub(),
                route.getFromHub(),
                route.getMoveDistance(),
                route.getTimeRequired()
        );
    }
}