package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.application.DeliveryFacade;
import com.springcloud.client.user.domain.DeliveryCommand;
import com.springcloud.client.user.domain.DeliveryInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class DeliveryController {

    private final DeliveryFacade deliveryFacade;

    @PostMapping("/hub-delivery-driver")
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> addHubDeliveryDriver(@RequestBody DeliveryDriverDto.DeliveryDriverRequest request) {
        DeliveryCommand command = request.toCommand();
        DeliveryInfo info = deliveryFacade.addHubDeliveryDriver(command);
        DeliveryDriverDto.DeliveryDriverResponse response = new DeliveryDriverDto.DeliveryDriverResponse(info);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hub-delivery-driver")
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> getHubDeliveryDriver() {
        DeliveryInfo info = deliveryFacade.getHubDeliveryDriver();
        DeliveryDriverDto.DeliveryDriverResponse response = new DeliveryDriverDto.DeliveryDriverResponse(info);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/company-delivery-driver")
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> addCompanyDeliveryDriver(@RequestBody DeliveryDriverDto.DeliveryDriverRequest request) {
        DeliveryCommand command = request.toCommand();
        DeliveryInfo info = deliveryFacade.addCompanyDeliveryDriver(command);
        DeliveryDriverDto.DeliveryDriverResponse response = new DeliveryDriverDto.DeliveryDriverResponse(info);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company-delivery-driver/{hubId}")
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> getCompanyDeliveryDriver(@PathVariable UUID hubId) {
        DeliveryInfo info = deliveryFacade.getCompanyDeliveryDriver(hubId);
        DeliveryDriverDto.DeliveryDriverResponse response = new DeliveryDriverDto.DeliveryDriverResponse(info);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/delivery-driver/delete")
    public ResponseEntity<Void> deleteDeliveryDriver(@RequestBody DeliveryDriverDto.DeliveryDriverRequest request) {
        DeliveryCommand command = request.toCommand();
        deliveryFacade.deleteDeliveryDriver(command);
        return ResponseEntity.noContent().build();
    }
}
