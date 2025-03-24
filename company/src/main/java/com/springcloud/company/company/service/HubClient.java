package com.springcloud.company.company.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubClient {
    @GetMapping("/hub/verifiedhub/{hubId}")
    Integer verifiedHub(@PathVariable("hubId") UUID hubId);

}
