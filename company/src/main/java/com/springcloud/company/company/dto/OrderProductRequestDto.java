package com.springcloud.company.company.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderProductRequestDto {
    private UUID recevingCompanyId;
    private UUID productId;
    private Integer quantity;
}
