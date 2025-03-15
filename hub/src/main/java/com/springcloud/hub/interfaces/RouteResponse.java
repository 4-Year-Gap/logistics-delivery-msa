package com.springcloud.hub.interfaces;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RouteResponse {
    private int code;
    private String currentDateTime;
    private String message;
    private Route route;

    @Data
    public static class Route {
        private List<Traoptimal> traoptimal;
    }

    @Data
    public static class Traoptimal {
        private Summary summary;
    }

    @Data
    public static class Summary {
        private List<List<BigDecimal>> bbox; // 경로 범위 좌표
        private String departureTime;
        private BigDecimal distance; // 이동 거리 (m)
        private long duration; // 소요 시간 (밀리초)
        private BigDecimal fuelPrice;
        private Goal goal;
        private Start start;
        private BigDecimal taxiFare;
        private BigDecimal tollFare;
    }

    @Data
    public static class Goal {
        private int dir;
        private List<BigDecimal> location; // 목표 지점 좌표 (경도, 위도)
    }

    @Data
    public static class Start {
        private List<BigDecimal> location; // 출발 지점 좌표 (경도, 위도)
    }
}
