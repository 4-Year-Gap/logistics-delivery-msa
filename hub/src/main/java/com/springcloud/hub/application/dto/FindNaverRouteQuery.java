package com.springcloud.hub.application.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

public record FindNaverRouteQuery(LocalTime timeRequired, BigDecimal moveDistance) {
}
