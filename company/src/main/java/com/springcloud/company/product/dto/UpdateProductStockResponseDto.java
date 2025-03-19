package com.springcloud.company.product.dto;

import com.springcloud.company.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductStockResponseDto {
    private UUID productId;
    private String productName;
    private int stock;

    public UpdateProductStockResponseDto(Product product) {
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.stock = product.getStock();
}


}


