package com.springcloud.hub.interfaces;


import java.util.UUID;

public record GetHubRouteRequestDto(UUID startHubId, UUID goalHubId) {
    public GetHubRouteRequestDto {
        if (startHubId == null || goalHubId == null) {
            throw new IllegalArgumentException("HUB ID가 존재해야 합니다.");
        }
    }
}