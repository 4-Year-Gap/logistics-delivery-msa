package com.springcloud.hub.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateHubRouteRequest(UUID id,
                                    BigDecimal moveDistance,
                                    LocalTime timeRequired) {
}
