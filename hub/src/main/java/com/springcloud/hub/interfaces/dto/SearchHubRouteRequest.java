package com.springcloud.hub.interfaces.dto;

import java.math.BigDecimal;

public record SearchHubRouteRequest(String address,
                                    BigDecimal latitude,
                                    BigDecimal longitude) {
}