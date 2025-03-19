package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.dto.UpdateHubRequest;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateHubCommand(UUID id,
                               String name,
                               String address,
                               BigDecimal latitude,
                               BigDecimal longitude) {
    public static UpdateHubCommand fromUpdateHubRequest(UpdateHubRequest updateHubRequest){
        return new UpdateHubCommand(
                updateHubRequest.id(),
                updateHubRequest.name(),
                updateHubRequest.address(),
                updateHubRequest.latitude(),
                updateHubRequest.longitude()
        );
    }

    public Hub toEntity() {
        return Hub.builder()
                .Id(id)
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
