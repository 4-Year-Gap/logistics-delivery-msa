package com.spring_cloud.eureka.client.order.presentation.controller;


import com.spring_cloud.eureka.client.order.application.service.OrderService;
import com.spring_cloud.eureka.client.order.common.ApiResponse;
import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import com.spring_cloud.eureka.client.order.presentation.dto.OrderUpdateRequest;
import com.spring_cloud.eureka.client.order.presentation.dto.request.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ApiResponse<?> createOrder(
            @RequestBody OrderCreateRequest orderCreateRequest,
            @Header(name = "X-USER-ID") UUID userId
            ) {

        OrderEntity customOrder = orderService.createOrder(orderCreateRequest,userId);

        return ApiResponse.created(customOrder);
    }


    @PatchMapping
    public ApiResponse<?> updateOrder(
            @Header(name = "X-USER-ID") UUID userId,
            @RequestBody OrderUpdateRequest orderUpdateRequest
    ) {
        orderService.updateOrder(orderUpdateRequest,userId);
        return ApiResponse.ok("일단 업데이트 성공");
    }


    @GetMapping("/{order_Id}")
    public ApiResponse<?> getOneOrderInformationById(@PathVariable UUID order_Id){


        return ApiResponse.ok(orderService.getOneOrderInformationById(order_Id));
    }

}
