package com.spring_cloud.eureka.client.order.presentation.dto.request;


import com.spring_cloud.eureka.client.order.domain.order.OrderEntityStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderUpdateRequest {
    private UUID orderId;
    private OrderEntityStatus orderEntityStatus;
}
