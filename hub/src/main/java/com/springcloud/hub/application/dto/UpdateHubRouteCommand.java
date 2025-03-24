package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.interfaces.dto.UpdateHubRouteRequest;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateHubRouteCommand(UUID id,
                                    BigDecimal moveDistanc,
                                    LocalTime timeRequired,
                                    UUID userId) {
    public static UpdateHubRouteCommand fromUpdateHubRouteRequest(UpdateHubRouteRequest updateHubRequest, UUID userId){
        return new UpdateHubRouteCommand(
                updateHubRequest.id(),
                updateHubRequest.moveDistance(),
                updateHubRequest.timeRequired(),
                userId
        );
    }

    public HubRoute toEntity(HubRoute hubRoute) {
        HubRoute route = HubRoute.builder()
                .Id(hubRoute.getId())
                .toHub(hubRoute.getToHub())
                .fromHub(hubRoute.getFromHub())
                .moveDistance(moveDistanc)
                .timeRequired(timeRequired)
                .build();

        route.update(String.valueOf(userId));

        return route;

    }
}
