package com.springcloud.client.delivery.presentation.dto;

import com.springcloud.client.delivery.application.DeliveryUpdateCommand;
import com.springcloud.client.delivery.domain.delivery.DeliveryStatusEnum;

import java.util.UUID;

public class DeliveryUpdateRequest {
    private UUID deliveryId;
    private DeliveryStatusEnum status;
    private UUID arrivedHub;

    public DeliveryUpdateCommand toCommand(){
        return DeliveryUpdateCommand.builder()
                .deliveryId(deliveryId)
                .status(status)
                .arrivedHub(arrivedHub)
                .build();
    }
}
