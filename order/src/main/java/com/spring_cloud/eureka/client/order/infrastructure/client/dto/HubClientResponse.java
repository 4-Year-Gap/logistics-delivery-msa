package com.spring_cloud.eureka.client.order.infrastructure.client.dto;


import com.spring_cloud.eureka.client.order.domain.delivery.DeliveryHubRoute;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class HubClientResponse {
    private List<HubRoute> shortestRoute;
    private Long estimatedTime;
    private Long estimatedDistance;
}
