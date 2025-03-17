package com.springcloud.hub.infrastructure.external;

import java.math.BigDecimal;

public interface NaverMapClient {
    String requestOptimalRoute(BigDecimal startLat, BigDecimal startLon, BigDecimal goalLat, BigDecimal goalLon);
}
