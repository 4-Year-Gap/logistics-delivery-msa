package com.springcloud.product.entity;

import com.springcloud.product.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;


import java.util.UUID;

@Getter
@Setter
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
    private String name;

    @Column(nullable = false)
    @Comment("출고 가능 수량")
    private Integer stock;


    public Product(ProductRequestDto requestDto) {
        this.userId = UUID.randomUUID();
        this.companyId= UUID.randomUUID();
        this.hubId = UUID.randomUUID();
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.stock = requestDto.getStock();

    }
}
