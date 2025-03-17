package com.springcloud.client.delivery.infrastructure.dto;



import com.springcloud.client.delivery.domain.delivery.DeliveryHubRoute;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HubClientResponse {
    private List<HubRoute> shortestRoute;
    private Long estimatedTime;
    private Long estimatedDistance;

    @Override
    public String toString() {
        return "HubClientResponse{" +
                "shortestRoute=" + shortestRoute +
                ", estimatedTime=" + estimatedTime +
                ", estimatedDistance=" + estimatedDistance +
                '}';
    }

    public List<DeliveryHubRoute> fromHubRoute() {
        return this.shortestRoute.stream()
                .map(DeliveryHubRoute::create)
                .collect(Collectors.toList());
    }

}
