package com.springcloud.company.product.dto;

import lombok.Getter;

@Getter
public class UpdateProductRequestDto {
    private String productName;
    private int productPrice;
    public int quantity;
}
