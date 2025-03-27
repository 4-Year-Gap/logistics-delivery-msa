package com.spring_cloud.eureka.client.order.application;

import com.spring_cloud.eureka.client.order.domain.order.OrderEntityStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


@Builder
@Getter
public class OrderUpdateCommand {
    private UUID orderId;
    private OrderEntityStatus orderEntityStatus;
    private UUID userId;
    private String userRole;
}
