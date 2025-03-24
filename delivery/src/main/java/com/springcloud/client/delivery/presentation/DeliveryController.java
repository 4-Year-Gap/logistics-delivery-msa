package com.springcloud.client.delivery.presentation;


import com.springcloud.client.delivery.application.service.DeliveryService;
import com.springcloud.client.delivery.common.ApiResponse;
import com.springcloud.client.delivery.domain.delivery.*;
import com.springcloud.client.delivery.infrastructure.client.HubClient;
import com.springcloud.client.delivery.infrastructure.client.UserClient;
import com.springcloud.client.delivery.infrastructure.dto.DeliveryDriverClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import com.springcloud.client.delivery.infrastructure.dto.UserInfoClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final HubClient hubClient;
    private final UserClient userClient;


    @GetMapping("/search")
    public Page<Delivery> getDeliveries(
            @Header("X-USER-ID") Integer userId,
            @Header("X-USER-ROLE") String role,
            Pageable pageable
    ){
        return deliveryService.getDeliveries(userId,role,pageable);
    }

    @GetMapping("/{deliveryId}")
    public ApiResponse<?> getDeliveries(
            @Header("X-USER-ID") Integer userId,
            @Header("X-USER-ROLE") String role,
            @PathVariable(name = "deliveryId") UUID deliveryId
    ){
        return ApiResponse.ok(deliveryService.getDelivery(userId,role,deliveryId));
    }

    @PatchMapping()
    public ApiResponse<?> updateDelivery(@RequestBody DeliveryUpdateRequest deliveryUpdateRequest){
        DeliveryUpdateCommand command = deliveryUpdateRequest.toCommand();
        deliveryService.updateDelivery(command);
        return ApiResponse.ok("update complete");
    }

    @DeleteMapping()
    public ApiResponse<?> deleteDelivery(@RequestBody DeliveryDeleteRequest deliveryDeleteRequest){

        DeliveryDeleteCommand command = deliveryDeleteRequest.toCommand();
        deliveryService.deleteDelivery(command);
        return ApiResponse.ok("delete complete");
    }

    @GetMapping("/hub")
    public UUID getOrderIdToHubId(@RequestParam UUID hubId, @RequestParam UUID orderId) {
        return deliveryService.getOrderIdToHubId(hubId,orderId);
    }

    @GetMapping("/hub/list")
    public List<UUID> getOrderIdListToHubId(@RequestParam UUID hubId) {
        return deliveryService.getOrderIdListToHubId(hubId);
    }

    @GetMapping("/deliver")
    public UUID getOrderIdToDeliver(@RequestParam UUID orderId, @RequestParam UUID userId){
        return deliveryService.getOrderIdToDeliver(orderId,userId);
    }


    @GetMapping("/deliver/list")
    public List<UUID> getOrderIdListToDeliver(@RequestParam UUID userId){
        return deliveryService.getOrderIdListToDeliver(userId);
    }


    @GetMapping("/test")
    public ApiResponse<?> deleteDelivery(){

        HubClientResponse<List<HubRoute>> list = hubClient.getRoute(UUID.fromString("86d7b72b-0270-11f0-87a5-0242ac130003"),UUID.fromString("86d6204c-0270-11f0-87a5-0242ac130003"));

        return ApiResponse.ok(list.getData());
    }
}
