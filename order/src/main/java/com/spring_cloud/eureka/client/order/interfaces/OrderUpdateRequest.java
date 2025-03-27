package com.spring_cloud.eureka.client.order.interfaces;


import com.spring_cloud.eureka.client.order.domain.order.OrderEntityStatus;
import com.spring_cloud.eureka.client.order.application.OrderUpdateCommand;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderUpdateRequest {
    private UUID orderId;
    private OrderEntityStatus orderEntityStatus;


    public OrderUpdateCommand toCommand(UUID userId,String userRole) {
        return OrderUpdateCommand.builder()
                .orderId(orderId)
                .orderEntityStatus(orderEntityStatus)
                .userId(userId)
                .userRole(userRole)
                .build();

    }
}
