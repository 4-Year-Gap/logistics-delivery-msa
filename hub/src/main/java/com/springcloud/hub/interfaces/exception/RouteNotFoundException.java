package com.springcloud.hub.interfaces.exception;

import java.util.UUID;

public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(UUID startHubId, UUID goalHubId) {
        super("출발지(" + startHubId + ")와 도착지(" + goalHubId + ") 사이의 최적 경로를 찾을 수 없습니다.");
    }
}