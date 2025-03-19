package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.application.DeliveryFacade;
import com.springcloud.client.user.domain.DeliveryInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class DeliveryController {

    private final DeliveryFacade deliveryFacade;

    @GetMapping("/hub-driver-assignment")
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> getHubDeliveryDriver() {
        DeliveryInfo info = deliveryFacade.getHubDeliveryDriver();
        DeliveryDriverDto.DeliveryDriverResponse response = new DeliveryDriverDto.DeliveryDriverResponse(info);
        return ResponseEntity.ok(response);
    }
}
