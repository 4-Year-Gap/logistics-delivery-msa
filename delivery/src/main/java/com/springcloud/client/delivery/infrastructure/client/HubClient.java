package com.springcloud.client.delivery.infrastructure.client;


import com.springcloud.client.delivery.common.ApiResponse;
import com.springcloud.client.delivery.config.FeignConfig;
import com.springcloud.client.delivery.infrastructure.dto.HubClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubRouteRequest;
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
