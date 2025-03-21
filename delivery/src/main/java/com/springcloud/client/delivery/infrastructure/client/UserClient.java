package com.springcloud.client.delivery.infrastructure.client;


import com.springcloud.client.delivery.common.ApiResponse;
import com.springcloud.client.delivery.config.FeignConfig;
import com.springcloud.client.delivery.infrastructure.dto.DeliveryDriverClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.UserInfoClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Component
@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    ApiResponse<UserInfoClientResponse> getUserInfo(@PathVariable Integer userId);

    @GetMapping("/api/users/hub-delivery-driver")
    DeliveryDriverClientResponse getRoute();

}
