package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.IdentityIntegrationCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityIntegrationDto implements Serializable {

    private UUID userId;
    private UUID hubId;
    private UUID companyId;
    private UUID orderId;
    private UUID deliveryId;

    public IdentityIntegrationCommand toCommand() {
        return IdentityIntegrationCommand.builder()
                .userId(this.userId)
                .hubId(this.hubId)
                .companyId(this.companyId)
                .orderId(this.orderId)
                .deliveryId(this.deliveryId)
                .build();
    }
}