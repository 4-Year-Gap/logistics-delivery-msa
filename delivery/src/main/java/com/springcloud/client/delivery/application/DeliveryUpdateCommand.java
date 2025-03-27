package com.springcloud.client.delivery.application;


import com.springcloud.client.delivery.domain.delivery.DeliveryStatusEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DeliveryUpdateCommand {
    private UUID deliveryId;
    private DeliveryStatusEnum status;
    private UUID arrivedHub;
}
