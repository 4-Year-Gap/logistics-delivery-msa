package com.springcloud.hub.interfaces;

import java.util.UUID;

public record CreateHubRouteRequest(UUID startHubId, UUID goalHubId) {
}
