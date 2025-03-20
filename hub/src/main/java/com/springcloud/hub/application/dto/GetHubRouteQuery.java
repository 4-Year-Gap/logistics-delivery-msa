package com.springcloud.hub.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.springcloud.hub.domain.entity.HubRoute;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record GetHubRouteQuery(
        int sequenceNumber,
        UUID hubId,
        String name,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal moveDistance,    // 이전 허브에서 현재 허브까지의 거리
        @JsonSerialize(using = LocalTimeSerializer.class)
        @JsonDeserialize(using = LocalTimeDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime timeRequired,     // 이전 허브에서 현재 허브까지의 시간
        BigDecimal totalDistance    // 출발점에서 현재 허브까지의 누적 거리
) implements Serializable {
    // 기본 허브 정보만 포함하는 생성자
    public GetHubRouteQuery(FindHubQuery hub, int sequenceNumber) {
        this(
                sequenceNumber,
                hub.id(),
                hub.name(),
                hub.latitude(),
                hub.longitude(),
                null,    // 첫 번째 허브는 이전 허브가 없으므로 null
                null,    // 첫 번째 허브는 이전 허브가 없으므로 null
                BigDecimal.ZERO  // 시작점은 누적 거리 0
        );
    }

    // 허브 정보와 경로 정보를 포함하는 생성자
    public GetHubRouteQuery(FindHubQuery hub, int sequenceNumber, BigDecimal moveDistance, LocalTime timeRequired, BigDecimal totalDistance) {
        this(
                sequenceNumber,
                hub.id(),
                hub.name(),
                hub.latitude(),
                hub.longitude(),
                moveDistance,
                timeRequired,
                totalDistance
        );
    }
}