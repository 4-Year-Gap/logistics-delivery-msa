package com.springcloud.hub.application;

import com.springcloud.hub.domain.entity.Hub;

public record GetHubResponseDto() {
    public static GetHubResponseDto from(Hub hub) {
        return new GetHubResponseDto(

        );
    }
}
