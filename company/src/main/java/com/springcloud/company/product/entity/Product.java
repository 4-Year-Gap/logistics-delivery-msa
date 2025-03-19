package com.springcloud.company.product.entity;

import com.springcloud.company.company.entity.Company;
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

    @Column(nullable = false)
    @Comment("보관 허브 ID")
    private UUID hubId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id", nullable = false)
    private Company company;

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
    public static Product create(String productName, Integer price, Integer stock, UUID userId, Company company) {
        Product product = new Product();
        product.userId = userId;
        product.company = company;
        product.hubId = company.getHubId();
        product.productName = productName;
        product.price = price;
        product.stock = stock;
        return product;
    }
}
