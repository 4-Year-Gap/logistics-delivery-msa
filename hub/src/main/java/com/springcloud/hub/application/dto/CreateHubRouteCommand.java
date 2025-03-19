package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.HubRoute;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record CreateHubRouteCommand(
        UUID id,
        UUID toHubId,
        UUID fromHubId,
        LocalTime timeRequired,
        BigDecimal moveDistance
) {
    public CreateHubRouteCommand(HubRoute route) {
        this(
                route.getId(),
                route.getToHub().getId(),
                route.getFromHub().getId(),
                route.getTimeRequired(),
                route.getMoveDistance()
        );
    }
}