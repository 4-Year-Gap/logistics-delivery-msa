package com.springcloud.company.product.dto;

import com.springcloud.company.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    //상품 ID
    private UUID id;
    //허브 ID
    private UUID hubId;
    //유저ID
    private UUID userId;
    //업체ID
    private UUID company;
    //상품명
    private String productName;
    //상품 가격
    private Integer price;
    //출고 가능 수량
    private Integer stock;


    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.hubId = product.getHubId();
        this.userId = product.getUserId();
        this.company = product.getCompany().getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.stock = product.getStock();

    }


}
