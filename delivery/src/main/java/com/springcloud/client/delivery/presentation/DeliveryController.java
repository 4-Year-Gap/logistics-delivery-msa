package com.springcloud.client.delivery.presentation;


import com.springcloud.client.delivery.application.service.DeliveryService;
import com.springcloud.client.delivery.common.ApiResponse;
import com.springcloud.client.delivery.domain.delivery.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {


    private final DeliveryService deliveryService;


    @GetMapping("/search")
    public Page<Delivery> getDeliveries(
            @Header("X-USER-ID") Integer userId,
            @Header("X-USER-ROLE") String role,
            Pageable pageable
    ){
        return deliveryService.getDeliveries(userId,role,pageable);
    }

    @GetMapping("/{deliveryId}")
    public ApiResponse<?> getDeliveries(
            @Header("X-USER-ID") Integer userId,
            @Header("X-USER-ROLE") String role,
            @PathVariable(name = "deliveryId") UUID deliveryId
    ){
        return ApiResponse.ok(deliveryService.getDelivery(userId,role,deliveryId));
    }



}
