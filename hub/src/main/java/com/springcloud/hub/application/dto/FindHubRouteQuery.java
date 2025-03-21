package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.HubRoute;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record FindHubRouteQuery(UUID id, BigDecimal moveDistance, LocalTime timeRequired) {
    public static FindHubRouteQuery fromHubRouteEntity(HubRoute hubRoute) {
        return new FindHubRouteQuery(
                hubRoute.getId(),
                hubRoute.getMoveDistance(),
                hubRoute.getTimeRequired()
        );
    }
}