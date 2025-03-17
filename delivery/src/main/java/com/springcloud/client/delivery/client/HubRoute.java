package com.springcloud.client.delivery.client;

import lombok.Getter;

import java.util.UUID;


@Getter
public class HubRoute {
    private Integer deliverySequence; // 부산 대전 경기
    private UUID startHub;
    private UUID destinationHub;
}
