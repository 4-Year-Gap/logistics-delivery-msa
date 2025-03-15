package com.spring_cloud.eureka.client.order.infrastructure.client;

import com.spring_cloud.eureka.client.order.common.ApiResponse;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.HubClientResponse;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.HubRouteRequest;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.UserInfoClientResponse;
import com.spring_cloud.eureka.client.order.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;


@Component
@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface UserInfoClient {

    @GetMapping("/api/user/{userId}")
    ApiResponse<UserInfoClientResponse> getUserInfo(@PathVariable UUID userId);
}
