package com.springcloud.client.user.interfaces;

import lombok.Getter;

import java.util.UUID;

@Getter
public class IdentityIntegrationDto {

    private String domain;
    private String eventType;
    private UUID userId;
    private UUID hubId;
    private UUID companyId;
    private UUID orderId;
    private UUID deliveryId;
}
