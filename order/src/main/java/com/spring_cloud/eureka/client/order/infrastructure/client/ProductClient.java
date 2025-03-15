package com.spring_cloud.eureka.client.order.infrastructure.client;


import com.spring_cloud.eureka.client.order.common.ApiResponse;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.ProductClientRequest;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.ProductClientResponse;
import com.spring_cloud.eureka.client.order.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Component
@FeignClient(name = "product-service",configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/api/products/{productId}")
    ApiResponse<ProductClientResponse> getProduct(@PathVariable("productId") UUID productId , @RequestBody ProductClientRequest request);

}