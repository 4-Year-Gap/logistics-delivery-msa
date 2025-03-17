package com.springcloud.hub.application;


import com.springcloud.hub.domain.entity.Hub;

import java.math.BigDecimal;
import java.util.UUID;

public record HubCommand(UUID id, String name, BigDecimal latitude, BigDecimal longitude) {
    public HubCommand(Hub hub) {
        this(
                hub.getId(),
                hub.getName(),
                hub.getLatitude(),
                hub.getLongitude()
        );
    }
}