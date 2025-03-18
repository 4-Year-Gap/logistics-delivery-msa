package com.springcloud.company.company.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderProductResponseDto {
    private UUID productId;
    private UUID startHub;
    private UUID endHub;

    public OrderProductResponseDto(UUID startHub, UUID id, UUID endHub) {
        this.productId = id;
        this.startHub = startHub;
        this.endHub = endHub;
    }
}
