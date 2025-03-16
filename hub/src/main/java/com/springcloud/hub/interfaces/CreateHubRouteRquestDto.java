package com.springcloud.hub.interfaces;

import java.util.UUID;

public record CreateHubRouteRquestDto(UUID startHubId, UUID goalHubId) {
}
