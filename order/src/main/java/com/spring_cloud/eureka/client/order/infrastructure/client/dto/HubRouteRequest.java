package com.spring_cloud.eureka.client.order.infrastructure.client.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HubRouteRequest {
    private UUID startHub;
    private UUID endHub;

    public static HubRouteRequest create(ProductClientResponse clientResponse){
        return HubRouteRequest.builder()
                .endHub(clientResponse.getEndHub())
                .startHub(clientResponse.getStartHub())
                .build();
    }

    @Override
    public String toString() {
        return "HubRouteRequest{" +
                "startHub=" + startHub
                +
                '}';
    }

}
