package com.spring_cloud.eureka.client.order.infrastructure.client;


import com.spring_cloud.eureka.client.order.config.FeignConfig;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.DeliveryClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;


@Component
@FeignClient(name = "delivery-service",configuration = FeignConfig.class)
public interface DeliveryClient {



    @GetMapping("/api/deliveries/deliver")
    UUID getOrderIdToDeliver(@RequestParam UUID orderId, @RequestParam UUID userId);

    @GetMapping("/api/deliveries/hub")
    UUID getOrderIdToHubId(@RequestParam UUID hubId, @RequestParam UUID orderId);

    @GetMapping("/api/deliveries/deliver/list")
    List<UUID> getOrderIdListToDeliver(@RequestParam UUID userId);

    @GetMapping("/api/deliveries/hub")
    List<UUID> getOrderIdListToHub(@RequestParam UUID hubId);
}