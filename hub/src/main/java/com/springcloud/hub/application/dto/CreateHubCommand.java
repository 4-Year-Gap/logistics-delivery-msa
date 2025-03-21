package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.dto.CreateHubRequest;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateHubCommand(String name,
                               String address,
                               BigDecimal latitude,
                               BigDecimal longitude) {
    public static CreateHubCommand fromCreateHubRequest(CreateHubRequest createHubRequest){
        return new CreateHubCommand(
                createHubRequest.name(),
                createHubRequest.address(),
                createHubRequest.latitude(),
                createHubRequest.longitude()
        );
    }

    public Hub toEntity() {
        return Hub.builder()
                .Id(UUID.randomUUID())
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
