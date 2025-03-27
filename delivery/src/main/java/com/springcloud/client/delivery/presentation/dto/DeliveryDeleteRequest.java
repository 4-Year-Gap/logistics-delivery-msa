package com.springcloud.client.delivery.presentation.dto;

import com.springcloud.client.delivery.application.DeliveryDeleteCommand;

import java.util.UUID;

public class DeliveryDeleteRequest {

    private UUID deliveryId;
    private String userName;

    public DeliveryDeleteCommand toCommand(){
        return DeliveryDeleteCommand.builder()
                .deliveryId(deliveryId)
                .userName(userName)
                .build();
    }
}
