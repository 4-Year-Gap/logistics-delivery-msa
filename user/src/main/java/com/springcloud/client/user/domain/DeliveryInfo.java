package com.springcloud.client.user.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DeliveryInfo {

    private final UUID deliveryDriverId;
    private final String username;
    private final String slackId;
    private final DeliveryDriverRole role;

    public DeliveryInfo(DeliveryDriver deliveryDriver) {
        this.deliveryDriverId = deliveryDriver.getDeliveryDriverId();
        this.username = deliveryDriver.getUser().getUsername();
        this.slackId = deliveryDriver.getUser().getSlackId();
        this.role = deliveryDriver.getRole();
    }
}
