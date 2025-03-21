package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.interfaces.dto.DeleteHubRouteRequest;

import java.util.UUID;

public record DeleteHubRouteCommand(UUID id) {
    public static DeleteHubRouteCommand fromDeleteHubRequest(DeleteHubRouteRequest deleteHubRouteRequest){
        return new DeleteHubRouteCommand(
                deleteHubRouteRequest.id()
        );
    }

    public HubRoute toEntity(HubRoute hubRoute) {
        hubRoute.delete("system");
        return hubRoute;
    }
}
