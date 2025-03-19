package com.springcloud.company.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateProductStockRequestDto {
    private UUID productId;
    private int stock;
}
