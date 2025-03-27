package com.spring_cloud.eureka.client.order.application;


import com.spring_cloud.eureka.client.order.domain.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<OrderEntity> getOrders(Pageable pageable, OrderSearchCondition orderSearchCondition) {
        return orderService.searchOrders(pageable,orderSearchCondition);
    }
}
