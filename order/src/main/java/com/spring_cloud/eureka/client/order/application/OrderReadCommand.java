package com.spring_cloud.eureka.client.order.application;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class OrderReadCommand {
    private UUID orderId;
    private UUID userId;
    private String userRole;
}
