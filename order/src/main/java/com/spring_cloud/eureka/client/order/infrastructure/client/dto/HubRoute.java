package com.spring_cloud.eureka.client.order.infrastructure.client.dto;

import lombok.Getter;

import java.util.UUID;


@Getter
public class HubRoute {
    private Integer deliverySequence; // 부산 대전 경기
    private UUID startHub;
    private UUID destinationHub;
}
