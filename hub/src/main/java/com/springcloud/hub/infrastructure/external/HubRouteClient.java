package com.springcloud.hub.infrastructure.external;

import com.springcloud.hub.application.HubRouteResultDto;
import com.springcloud.hub.interfaces.exception.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubRouteClient {
    @GetMapping("/hub/routes/shortest-path")
    ResponseEntity<ResponseDto<List<HubRouteResultDto>>> findShortestPath(
            @RequestParam UUID startHubId,
            @RequestParam UUID goalHubId);
}



