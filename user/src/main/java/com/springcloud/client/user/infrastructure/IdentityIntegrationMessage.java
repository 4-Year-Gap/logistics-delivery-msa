package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.IdentityIntegrationCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityIntegrationMessage implements Serializable {

    private UUID userId;
    private UUID hubId;
    private UUID companyId;
    private UUID orderId;
    private UUID deliveryId;

    public IdentityIntegrationCommand toCommand(String key) {
        return IdentityIntegrationCommand.builder()
                .domain(key.split(":")[0])
                .eventType(key.split(":")[1])
                .userId(this.userId)
                .hubId(this.hubId)
                .companyId(this.companyId)
                .orderIdList(this.orderId == null ? new ArrayList<>() : new ArrayList<>(List.of(this.orderId)))
                .deliveryId(this.deliveryId)
                .build();
    }
}