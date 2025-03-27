package com.spring_cloud.eureka.client.order.infrastructure.client;


import com.spring_cloud.eureka.client.order.infrastructure.client.dto.ProductClientRequest;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.ProductClientResponse;
import com.spring_cloud.eureka.client.order.config.FeignConfig;
import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Component
@FeignClient(name = "company-service",configuration = FeignConfig.class)
public interface ProductClient {

    @PostMapping("/api/companies/check")
    ProductClientResponse getProduct(@RequestBody ProductClientRequest request);

    @GetMapping("/api/companies/companyId")
    UUID getCompanyId(@RequestParam UUID userId);
}