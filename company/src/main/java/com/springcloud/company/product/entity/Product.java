package com.springcloud.company.product.entity;

import com.springcloud.company.company.entity.Company;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;


import java.time.LocalDateTime;
import java.util.UUID;

@Where(clause = "deleted_at IS NULL")
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
    @Setter
    private Integer stock;



    //companyId에 맞는 product 가져오기


    //정적 팩토리 메서드(create 메서드) 사용
    public static Product create(String productName, Integer price, Integer stock, UUID userId, Company company) {
        Product product = new Product();
        product.userId = userId;
        product.company = company;
        product.hubId = company.getHubId();
        product.productName = productName;
        product.price = price;
        product.stock = stock;
        product.createdBy = String.valueOf(userId);
        return product;
    }


    public void deleteProduct(UUID userId) {
        delete(UUID.fromString(String.valueOf(userId)));
    }

    public void updateQuantity(int quantity) {
        if (quantity > 0) {
            // 양수일 경우: 재고 추가
            this.stock += quantity;
        } else {
            // 음수일 경우: 재고 차감 로직
            int absQuantity = Math.abs(quantity); // 절대값 변환
            if (this.stock < absQuantity) {
                throw new IllegalArgumentException("재고 부족");
            }
            this.stock -= absQuantity; // 재고 차감
        }
    }

    public void updateProduct(String productName, Integer productPrice, Integer quantity, UUID userId) {
        // productName이 null이 아닐 경우에만 업데이트
        if (productName != null) {
            this.productName = productName;
        }

        // productPrice가 null이 아닐 경우에만 업데이트
        if (productPrice != null) {
            this.price = productPrice;
        }

        // quantity가 null이 아닐 경우에만 처리
        if (quantity != null) {
            if (quantity > 0) {
                this.stock += quantity;  // 양수일 경우: 재고 추가
            } else if (quantity < 0) {
                int quantityToDeduct = -quantity;  // 음수에서 양수로 변환하여 차감
                if (this.stock < quantityToDeduct) {
                    throw new IllegalArgumentException("재고 부족: 요청한 차감량이 재고보다 많습니다.");
                }
                this.stock -= quantityToDeduct;  // 재고 차감
            }
        }

        // 업데이트 시간과 사용자 정보는 항상 갱신
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = String.valueOf(userId);
    }
}
