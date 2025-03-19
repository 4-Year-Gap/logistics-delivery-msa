package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.dto.DeleteHubRequest;
import com.springcloud.hub.interfaces.dto.UpdateHubRequest;

import java.math.BigDecimal;
import java.util.UUID;

public record DeleteHubCommand(UUID id) {
    public static DeleteHubCommand fromDeleteHubRequest(DeleteHubRequest deleteHubRequest){
        return new DeleteHubCommand(
                deleteHubRequest.id()
        );
    }

    public Hub toEntity() {
        return Hub.builder()
                .Id(id)
                .build();
    }
}
