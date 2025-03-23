package com.springcloud.hub.interfaces.external;

import com.springcloud.hub.infrastructure.external.OrderServiceClient;
import com.springcloud.hub.interfaces.exception.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderServiceController {

    private final OrderServiceClient orderServiceClient;

    @GetMapping("/test")
    public ResponseEntity<ResponseDto<String>> getOptimalRoute() {

        orderServiceClient.kafkaTest("09316eb7-89d8-4313-84e6-1703711face4");

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success("success"));
    }
}
