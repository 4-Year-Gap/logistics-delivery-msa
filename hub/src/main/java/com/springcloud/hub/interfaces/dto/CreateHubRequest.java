package com.springcloud.hub.interfaces.dto;

import java.math.BigDecimal;

public record CreateHubRequest(String name,
                               String address,
                               BigDecimal latitude,
                               BigDecimal longitude) {
}
