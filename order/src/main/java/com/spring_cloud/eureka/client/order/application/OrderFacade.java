package com.spring_cloud.eureka.client.order.application;


import com.spring_cloud.eureka.client.order.domain.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderFacade{

    private final OrderService orderService;

    public OrderEntity createOrder(OrderCreateCommand command){
        return orderService.createOrder(command);
    }

    public OrderUpdateInfo updateOrder(OrderUpdateCommand command){
        return orderService.updateOrder(command);
    }

    public OrderEntity getOneOrderInformationById(UUID orderId) {
        return orderService.getOneOrderInformationById(orderId);
    }
}
