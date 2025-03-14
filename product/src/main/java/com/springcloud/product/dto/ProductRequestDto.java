package com.springcloud.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    //상품명
    private String name;
    //상품 가격
    private Integer price;
    //출고 가능 수량
    private Integer stock;
}
