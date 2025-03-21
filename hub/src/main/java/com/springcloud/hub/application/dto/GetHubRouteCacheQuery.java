package com.springcloud.hub.application.dto;

import com.springcloud.hub.interfaces.dto.GetHubRouteRequest;

import java.util.UUID;

public record GetHubRouteCacheQuery(UUID startHubId, UUID goalHubId){
    public GetHubRouteCacheQuery {
        if (startHubId == null || goalHubId == null) {
            throw new IllegalArgumentException("HUB ID가 존재해야 합니다.");
        }
    }

    public static GetHubRouteCacheQuery fromRequestParam(GetHubRouteRequest request
    ) {
        return new GetHubRouteCacheQuery(
                request.startHubId(),
                request.goalHubId()
        );
    }
}
