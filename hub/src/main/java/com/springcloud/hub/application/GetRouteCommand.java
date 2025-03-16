package com.springcloud.hub.application;


import java.util.UUID;

public record GetRouteCommand(UUID startHubId, UUID goalHubId) {
    public GetRouteCommand {
        if (startHubId == null || goalHubId == null) {
            throw new IllegalArgumentException("Hub ID cannot be null");
        }
    }
}