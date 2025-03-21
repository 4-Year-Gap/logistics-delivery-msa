package com.springcloud.client.delivery.domain.delivery;


import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DeliveryDeleteCommand {

    private UUID deliveryId;
    private String userName;
}
