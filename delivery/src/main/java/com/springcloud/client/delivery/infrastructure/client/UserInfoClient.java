package com.springcloud.client.delivery.infrastructure.client;


import com.springcloud.client.delivery.common.ApiResponse;
import com.springcloud.client.delivery.config.FeignConfig;
import com.springcloud.client.delivery.infrastructure.dto.UserInfoClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Component
@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface UserInfoClient {

    @GetMapping("/api/user/{userId}")
    ApiResponse<UserInfoClientResponse> getUserInfo(@PathVariable Integer userId);
}
