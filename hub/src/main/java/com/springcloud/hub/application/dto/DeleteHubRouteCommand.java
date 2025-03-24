package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.interfaces.dto.DeleteHubRouteRequest;

import java.util.UUID;

public record DeleteHubRouteCommand(UUID id,
                                    UUID userId) {
    public static DeleteHubRouteCommand fromDeleteHubRequest(DeleteHubRouteRequest deleteHubRouteRequest, UUID userId){
        return new DeleteHubRouteCommand(
                deleteHubRouteRequest.id(),
                userId

        );
    }

    public HubRoute toEntity(HubRoute hubRoute) {
        hubRoute.delete(String.valueOf(userId));
        return hubRoute;
    }
}
