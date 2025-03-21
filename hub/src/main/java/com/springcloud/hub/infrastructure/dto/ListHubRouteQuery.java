package com.springcloud.hub.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListHubRouteQuery {
    private UUID id;
    private String fromAddress;
    private String toAddress;
    private BigDecimal moveDistance;
    private LocalTime timeRequired;
}