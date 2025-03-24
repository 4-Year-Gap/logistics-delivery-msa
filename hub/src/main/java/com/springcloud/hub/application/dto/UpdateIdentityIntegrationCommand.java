package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;

import java.util.UUID;

public record UpdateIdentityIntegrationCommand(UUID hubId,
                                               UUID userId){

    public static UpdateIdentityIntegrationCommand fromEntity(Hub hub) {
        return new UpdateIdentityIntegrationCommand(
                hub.getId(),
                hub.getUserId()
        );
    }
}
