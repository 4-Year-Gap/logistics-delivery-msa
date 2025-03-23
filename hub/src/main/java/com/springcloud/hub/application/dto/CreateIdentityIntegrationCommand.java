package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.dto.CreateHubRequest;
import org.apache.kafka.common.serialization.Serializer;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateIdentityIntegrationCommand(UUID hubId,
                                               UUID userId){

    public static CreateIdentityIntegrationCommand fromEntity(Hub hub) {
        return new CreateIdentityIntegrationCommand(
                hub.getId(),
                hub.getUserId()
        );
    }
}
