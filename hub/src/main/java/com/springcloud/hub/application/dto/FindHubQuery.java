package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;

import java.math.BigDecimal;
import java.util.UUID;

public record FindHubQuery(UUID id, String name, BigDecimal latitude, BigDecimal longitude) {
    public FindHubQuery(Hub hub) {
        this(
                hub.getId(),
                hub.getName(),
                hub.getLatitude(),
                hub.getLongitude()
        );
    }
}