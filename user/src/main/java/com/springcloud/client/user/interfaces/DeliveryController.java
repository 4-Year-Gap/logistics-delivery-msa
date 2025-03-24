package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.application.DeliveryFacade;
import com.springcloud.client.user.domain.DeliveryCommand;
import com.springcloud.client.user.domain.DeliveryInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class DeliveryController {

    private final DeliveryFacade deliveryFacade;

    @PostMapping("/hub-delivery-driver")
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> registerHubDeliveryDriver(HttpServletRequest httpServletRequest, @RequestBody DeliveryDriverDto.DeliveryDriverRequest request) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        DeliveryCommand command = request.toCommand();
        DeliveryInfo info = deliveryFacade.registerHubDeliveryDriver(userInfoHeader, command);
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
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> registerCompanyDeliveryDriver(HttpServletRequest httpServletRequest, @RequestBody DeliveryDriverDto.DeliveryDriverRequest request) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        DeliveryCommand command = request.toCommand();
        DeliveryInfo info = deliveryFacade.registerCompanyDeliveryDriver(userInfoHeader, command);
        DeliveryDriverDto.DeliveryDriverResponse response = new DeliveryDriverDto.DeliveryDriverResponse(info);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company-delivery-driver/{hubId}")
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> getCompanyDeliveryDriver(@PathVariable UUID hubId) {
        DeliveryInfo info = deliveryFacade.getCompanyDeliveryDriver(hubId);
        DeliveryDriverDto.DeliveryDriverResponse response = new DeliveryDriverDto.DeliveryDriverResponse(info);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/delivery-driver/{deliveryDriverId}")
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> updateDeliveryDriver(HttpServletRequest httpServletRequest, @PathVariable UUID deliveryDriverId, @RequestBody DeliveryDriverDto.DeliveryDriverRequest request) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        DeliveryCommand command = request.toCommand(deliveryDriverId);
        DeliveryInfo info = deliveryFacade.updateDeliveryDriver(userInfoHeader, command);
        DeliveryDriverDto.DeliveryDriverResponse response = new DeliveryDriverDto.DeliveryDriverResponse(info);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/delivery-driver/delete")
    public ResponseEntity<Void> deleteDeliveryDriver(HttpServletRequest httpServletRequest, @RequestBody DeliveryDriverDto.DeliveryDriverRequest request) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        DeliveryCommand command = request.toCommand();
        deliveryFacade.deleteDeliveryDriver(userInfoHeader, command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/delivery-driver/{deliveryDriverId}")
    public ResponseEntity<DeliveryDriverDto.DeliveryDriverResponse> getDeliveryDriver(HttpServletRequest httpServletRequest, @PathVariable UUID deliveryDriverId) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        DeliveryInfo info = deliveryFacade.getDeliveryDriver(userInfoHeader, deliveryDriverId);
        DeliveryDriverDto.DeliveryDriverResponse response = new DeliveryDriverDto.DeliveryDriverResponse(info);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delivery-drivers")
    public ResponseEntity<Page<DeliveryDriverDto.DeliveryDriverResponse>> getAllDeliveryDrivers(HttpServletRequest httpServletRequest, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "desc") String order) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        Page<DeliveryInfo> pageInfo = deliveryFacade.getAllDeliveryDrivers(userInfoHeader, page, size, sortBy, order);
        Page<DeliveryDriverDto.DeliveryDriverResponse> response = pageInfo.map(DeliveryDriverDto.DeliveryDriverResponse::new);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delivery-drivers/search/{keyword}")
    public ResponseEntity<Page<DeliveryDriverDto.DeliveryDriverResponse>> searchDeliveryDrivers(HttpServletRequest httpServletRequest, @PathVariable String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "desc") String order) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        Page<DeliveryInfo> pageInfo = deliveryFacade.searchDeliveryDrivers(userInfoHeader, keyword, page, size, sortBy, order);
        Page<DeliveryDriverDto.DeliveryDriverResponse> response = pageInfo.map(DeliveryDriverDto.DeliveryDriverResponse::new);
        return ResponseEntity.ok(response);
    }
}
