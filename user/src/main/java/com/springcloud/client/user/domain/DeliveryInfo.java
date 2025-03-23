package com.springcloud.client.user.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DeliveryInfo {

    private final UUID userId;
    private final UUID deliveryDriverId;
    private final String username;
    private final String slackId;
    private final DeliveryDriverRole role;
    private final UUID hubId;

    public DeliveryInfo(DeliveryDriver deliveryDriver) {
        this.userId = deliveryDriver.getUser().getUserId();
        this.deliveryDriverId = deliveryDriver.getDeliveryDriverId();
        this.username = deliveryDriver.getUser().getUsername();
        this.slackId = deliveryDriver.getUser().getSlackId();
        this.role = deliveryDriver.getRole();
        this.hubId = deliveryDriver.getHubId();
    }
}