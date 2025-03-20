package com.springcloud.hub.interfaces.dto;


import java.util.UUID;

public record GetHubRouteRequest(UUID startHubId, UUID goalHubId) {
    public GetHubRouteRequest {
        if (startHubId == null || goalHubId == null) {
            throw new IllegalArgumentException("HUB ID가 존재해야 합니다.");
        }
    }
}