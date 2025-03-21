package com.spring_cloud.eureka.client.order.infrastructure.client;


import com.spring_cloud.eureka.client.order.config.FeignConfig;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.DeliveryClientResponse;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.ProductClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;


@Component
@FeignClient(name = "delivery-service",configuration = FeignConfig.class)
public interface DeliveryClient {

    @GetMapping("/api/deliveries/orderId")
    DeliveryClientResponse checkOrderId(@RequestParam UUID orderId, @RequestParam String userRole, @RequestParam UUID userId);

}