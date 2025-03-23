package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;

import java.util.UUID;

public record DeleteIdentityIntegrationCommand(UUID hubId){

    public static DeleteIdentityIntegrationCommand fromEntity(Hub hub) {
        return new DeleteIdentityIntegrationCommand(
                hub.getId()
        );
    }
}
