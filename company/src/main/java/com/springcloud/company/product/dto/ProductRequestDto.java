package com.springcloud.company.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductRequestDto {
    //업체명
    private UUID companyId;
    //상품명
    private String productName;
    //상품 가격
    private Integer price;
    //출고 가능 수량
    private Integer stock;
}
