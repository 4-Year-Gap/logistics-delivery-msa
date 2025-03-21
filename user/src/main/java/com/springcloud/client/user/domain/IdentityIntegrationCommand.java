package com.springcloud.client.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class IdentityIntegrationCommand {

    private UUID userId;
    private UUID hubId;
    private UUID companyId;
    private UUID orderId;
    private UUID deliveryId;
}
