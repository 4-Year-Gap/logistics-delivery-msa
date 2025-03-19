package com.springcloud.client.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DeliveryCommand {

    private UUID userId;

    public DeliveryDriver toEntity(User user, int deliveryOrderNumber) {
        return DeliveryDriver.builder()
                .user(user)
                .deliveryOrderNumber(deliveryOrderNumber)
                .role(DeliveryDriverRole.HUB)
                .build();
    }
}
