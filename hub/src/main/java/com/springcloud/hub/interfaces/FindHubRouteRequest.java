package com.springcloud.hub.interfaces;


import java.util.UUID;

public record FindHubRouteRequest(UUID startHubId, UUID goalHubId) {
    public FindHubRouteRequest {
        if (startHubId == null || goalHubId == null) {
            throw new IllegalArgumentException("HUB ID가 존재해야 합니다.");
        }
    }
}