package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.dto.DeleteHubRequest;
import com.springcloud.hub.interfaces.dto.UpdateHubRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DeleteHubCommand(UUID id) {
    public static DeleteHubCommand fromDeleteHubRequest(DeleteHubRequest deleteHubRequest){
        return new DeleteHubCommand(
                deleteHubRequest.id()
        );
    }

    public Hub toEntity(Hub hub) {
        hub.delete("system"); // 삭제 일시와 삭제자만 설정
        return hub;
    }
}
