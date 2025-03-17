package com.springcloud.hub.application;

import com.springcloud.hub.domain.entity.Hub;

import java.math.BigDecimal;
import java.util.UUID;

public record HubDto(UUID id, String name, BigDecimal latitude, BigDecimal longitude) {
    public HubDto(Hub hub) {
        this(
                hub.getId(),
                hub.getName(),
                hub.getLatitude(),
                hub.getLongitude()
        );
    }
}