package com.spring_cloud.eureka.client.order.infrastructure.client;


import com.spring_cloud.eureka.client.order.infrastructure.client.dto.ProductClientRequest;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.ProductClientResponse;
import com.spring_cloud.eureka.client.order.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "company-service",configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/api/companies")
    ProductClientResponse getProduct(@RequestBody ProductClientRequest request);

}