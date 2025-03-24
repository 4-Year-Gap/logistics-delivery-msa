package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.dto.DeleteHubRequest;
import com.springcloud.hub.interfaces.dto.UpdateHubRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DeleteHubCommand(UUID id,
                               UUID userId) {
    public static DeleteHubCommand fromDeleteHubRequest(DeleteHubRequest deleteHubRequest, UUID userId){
        return new DeleteHubCommand(
                deleteHubRequest.id(),
                userId
        );
    }

    public Hub toEntity(Hub hub) {
        hub.delete(String.valueOf(userId));
        return hub;
    }
}
