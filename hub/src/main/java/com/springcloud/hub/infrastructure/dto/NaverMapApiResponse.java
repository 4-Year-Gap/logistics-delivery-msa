package com.springcloud.hub.infrastructure.dto;

import java.math.BigDecimal;
import java.util.List;

public record NaverMapApiResponse(
        int code,
        String currentDateTime,
        String message,
        Route route
) {
    public record Route(List<Traoptimal> traoptimal) {}

    public record Traoptimal(Summary summary) {}

    public record Summary(
            BigDecimal distance, // 이동 거리 (m)
            long duration   // 소요 시간 (밀리초)
            ) {}
}
