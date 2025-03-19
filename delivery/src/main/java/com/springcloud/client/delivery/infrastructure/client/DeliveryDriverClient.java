package com.springcloud.client.delivery.infrastructure.client;


import com.springcloud.client.delivery.config.FeignConfig;
import com.springcloud.client.delivery.infrastructure.dto.DeliveryDriverClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface DeliveryDriverClient {

    @GetMapping("/api/users/hub-delivery-driver")
    DeliveryDriverClientResponse getRoute();
}
