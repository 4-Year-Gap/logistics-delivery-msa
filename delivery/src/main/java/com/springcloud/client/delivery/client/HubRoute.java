package com.springcloud.client.delivery.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@AllArgsConstructor
@Getter
public class HubRoute {
    private Integer deliverySequence; // 부산 대전 경기
    private UUID startHub;
    private UUID destinationHub;
    public static HubRoute create(Integer deliverySequence,UUID startHub,UUID destinationHub){
        return new HubRoute(deliverySequence,startHub,destinationHub);

    }
}
