package com.spring_cloud.eureka.client.order.infrastructure.client;


import com.spring_cloud.eureka.client.order.common.ApiResponse;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.HubClientResponse;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.HubRouteRequest;
import com.spring_cloud.eureka.client.order.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Component
@FeignClient(name = "hub-service",configuration = FeignConfig.class)
public interface HubClient {

    @GetMapping("/api/hubs/routes")
    ApiResponse<HubClientResponse> getRoute(@RequestBody HubRouteRequest hubRouteRequest);
}
