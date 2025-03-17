package com.springcloud.client.delivery.infrastructure.dto;

import com.springcloud.client.delivery.config.OrderCreateEvent;
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

    public static HubRouteRequest create(OrderCreateEvent event){
        return HubRouteRequest.builder()
                .endHub(event.getEndHub())
                .startHub(event.getStartHub())
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
