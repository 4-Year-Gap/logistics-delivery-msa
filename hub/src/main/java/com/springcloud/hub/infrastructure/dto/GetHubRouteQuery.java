package com.springcloud.hub.infrastructure.dto;

import com.springcloud.hub.domain.entity.Hub;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetHubRouteQuery {
    private UUID id;
    private UUID toHubId;
    private UUID fromHubId;
    private Hub toHub;
    private Hub fromHub;
    private BigDecimal moveDistance;
    private LocalTime timeRequired;
}
