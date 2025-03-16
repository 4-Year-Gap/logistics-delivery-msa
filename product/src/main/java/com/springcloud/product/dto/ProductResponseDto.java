package com.springcloud.product.dto;

import com.springcloud.product.entity.Product;
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
    private UUID Id;
    //허브 ID
    private UUID hub_id;
    //유저ID
    private UUID user_id;
    //업체ID
    private UUID company_id;
    //상품명
    private String productName;
    //상품 가격
    private Integer price;
    //출고 가능 수량
    private Integer stock;


    public ProductResponseDto(Product product) {
        this.Id = product.getId();
        this.hub_id = product.getHubId();
        this.user_id = product.getUserId();
        this.company_id = product.getCompanyId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.stock = product.getStock();

    }


}
