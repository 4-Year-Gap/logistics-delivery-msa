package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.dto.UpdateHubRequest;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateHubCommand(UUID id,
                               String name,
                               String address,
                               BigDecimal latitude,
                               BigDecimal longitude,
                               UUID userId) {
    public static UpdateHubCommand fromUpdateHubRequest(UpdateHubRequest updateHubRequest, UUID userId){
        return new UpdateHubCommand(
                updateHubRequest.id(),
                updateHubRequest.name(),
                updateHubRequest.address(),
                updateHubRequest.latitude(),
                updateHubRequest.longitude(),
                userId
        );
    }

    public Hub toEntity(Hub hub) {
        Hub hubBuild = Hub.builder()
                .Id(hub.getId())
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .userId(userId)
                .build();

        hubBuild.update(String.valueOf(userId));

        return hubBuild;
    }
}
