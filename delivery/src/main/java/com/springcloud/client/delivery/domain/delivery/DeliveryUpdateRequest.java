package com.springcloud.client.delivery.domain.delivery;

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
