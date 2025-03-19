package com.springcloud.hub.infrastructure.external;

import com.springcloud.hub.application.dto.GetHubRouteQuery;
import com.springcloud.hub.interfaces.exception.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
    @GetMapping("/api/order/kafkaTest/{testOrderId}")
    ResponseEntity<ResponseDto<List<GetHubRouteQuery>>> kafkaTest(@PathVariable(name = "testOrderId") String orderId);
}



