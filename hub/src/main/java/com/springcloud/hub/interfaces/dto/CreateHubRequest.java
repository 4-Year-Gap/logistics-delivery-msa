package com.springcloud.hub.interfaces.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateHubRequest(String name,
                               String address,
                               BigDecimal latitude,
                               BigDecimal longitude) {
}
