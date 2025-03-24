package com.springcloud.client.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DeliveryCommand {

    private UUID userId;
    private UUID hubId;
    private UUID deliveryDriverId;
    private String username;
    private String slackId;
    private DeliveryDriverRole role;

    public DeliveryDriver toEntity(User user, int deliveryOrderNumber) {
        return DeliveryDriver.builder()
                .user(user)
                .hubId(this.hubId)
                .deliveryOrderNumber(deliveryOrderNumber)
                .role(hubId == null ? DeliveryDriverRole.HUB : DeliveryDriverRole.COMPANY)
                .build();
    }
}