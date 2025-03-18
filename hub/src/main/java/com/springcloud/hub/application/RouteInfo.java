package com.springcloud.hub.application;

import java.math.BigDecimal;
import java.time.LocalTime;

public record RouteInfo(LocalTime timeRequired, BigDecimal moveDistance) {
}
