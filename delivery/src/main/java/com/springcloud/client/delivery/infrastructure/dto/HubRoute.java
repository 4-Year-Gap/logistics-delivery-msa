package com.springcloud.client.delivery.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class HubRoute {

    private Integer sequenceNumber;
    private UUID hubId;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal moveDistance;
    private LocalTime timeRequired;
    private BigDecimal totalDistance;

}