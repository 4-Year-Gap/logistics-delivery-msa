package com.spring_cloud.eureka.client.order.application;


import com.spring_cloud.eureka.client.order.domain.order.*;
import com.spring_cloud.eureka.client.order.interfaces.OrderReadCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public OrderEntity getOneOrderInformationById(OrderReadCommand command) {
        return orderService.getOneOrderInformationById(command);
    }

//    public List<OrderEntity> getOrders(Pageable pageable, OrderSearchCondition orderSearchCondition) {
//        return orderService.searchOrders(pageable,orderSearchCondition);
//    }
}
