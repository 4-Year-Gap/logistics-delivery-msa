package com.springcloud.client.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class IdentityIntegrationCommand {

    private String domain;
    private String eventType;
    private UUID userId;
    private List<UUID> orderIdList;
    private UUID hubId;
    private UUID companyId;
    private UUID deliveryId;

    public IdentityIntegrationCacheData toCacheData() {
        return IdentityIntegrationCacheData.builder()
                .userId(this.userId)
                .hubId(this.hubId)
                .companyId(this.companyId)
                .orderIdList(this.orderIdList)
                .deliveryId(this.deliveryId)
                .build();
    }
}
