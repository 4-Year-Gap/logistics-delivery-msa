package com.springcloud.client.delivery.infrastructure.client;


import com.springcloud.client.delivery.config.FeignConfig;
import com.springcloud.client.delivery.infrastructure.dto.HubClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;


@Component
@FeignClient(name = "hub-service",configuration = FeignConfig.class)
public interface HubClient {

    @GetMapping("/api/hub/routes/shortest-path")
    HubClientResponse<List<HubRoute>> getRoute(@RequestParam UUID startHubId, @RequestParam UUID goalHubId);

}