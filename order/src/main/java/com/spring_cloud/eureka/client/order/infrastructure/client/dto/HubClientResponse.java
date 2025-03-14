package com.spring_cloud.eureka.client.order.infrastructure.client.dto;


import com.spring_cloud.eureka.client.order.domain.delivery.DeliveryHubRoute;
import lombok.Getter;

import java.util.List;

@Getter
public class HubClientResponse {

    List<HubRoute> shortestRoute;

}
