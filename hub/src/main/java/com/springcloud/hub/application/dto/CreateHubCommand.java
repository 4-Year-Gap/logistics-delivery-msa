package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.dto.CreateHubRequest;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateHubCommand(String name,
                               String address,
                               BigDecimal latitude,
                               BigDecimal longitude,
                               UUID userId) {
    public static CreateHubCommand fromCreateHubRequest(CreateHubRequest createHubRequest, UUID userId){
        return new CreateHubCommand(
                createHubRequest.name(),
                createHubRequest.address(),
                createHubRequest.latitude(),
                createHubRequest.longitude(),
                userId
        );
    }

    public Hub toEntity() {
        Hub hubBuild = Hub.builder()
                .Id(UUID.randomUUID())
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .userId(userId)
                .build();

        hubBuild.create(String.valueOf(userId));

        return hubBuild;
    }
}
