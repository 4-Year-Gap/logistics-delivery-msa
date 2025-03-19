package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.HubRoute;

import java.util.List;

public record HubRouteListCommand(List<CreateHubRouteCommand> hubs) {
    public static HubRouteListCommand fromEntities(List<HubRoute> entityHub) {
        return new HubRouteListCommand(entityHub.stream().map(CreateHubRouteCommand::new).toList());
    }
}