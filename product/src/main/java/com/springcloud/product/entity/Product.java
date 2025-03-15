package com.springcloud.product.entity;

import com.springcloud.product.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;


import java.util.UUID;

@Getter
@Entity
@Table(name= "product") //매핑할 테이블명
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{


    @Id
    @Column(name = "product_id")
    @UuidGenerator
    @Comment("상품 ID")
    private UUID id;

    @Column
    @Comment("업체 ID")
    private UUID companyId;

    @Column
    @Comment("보관 허브ID")
    private UUID hubId;

    @Column
    @Comment("유저 ID")
    private UUID userId;

    @Column(nullable = false)
    @Comment("상품 가격")
    private Integer price;

    @Column(name = "name", nullable = false)
    @Comment("상품명")
    private String productName;

    @Column(nullable = false)
    @Comment("출고 가능 수량")
    private Integer stock;

    //정적 팩토리 메서드(create 메서드) 사용
    public static Product create(String productName, Integer price, Integer stock) {
        Product product = new Product();
        product.userId = UUID.randomUUID();
        product.companyId= UUID.randomUUID();
        product.hubId = UUID.randomUUID();
        product.productName = productName;
        product.price = price;
        product.stock = stock;
        return product;
    }
}
