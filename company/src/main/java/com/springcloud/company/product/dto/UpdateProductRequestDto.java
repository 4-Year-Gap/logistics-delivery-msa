package com.springcloud.company.product.dto;

import lombok.Getter;

@Getter
public class UpdateProductRequestDto {
    private String productName;
    private Integer productPrice;
    public Integer quantity;
}
