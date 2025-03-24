package com.springcloud.company.company.entity;

import com.springcloud.company.product.entity.BaseEntity;
import com.springcloud.company.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Entity
@Table(name = "company")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
public class Company extends BaseEntity {
    @Id
    @Column(name = "company_id")
    @UuidGenerator
    @Comment("업체 ID")
    private UUID id;

    @Column
    @Comment("허브ID")
    private UUID hubId;

    @Column
    @Comment("유저ID")
    private UUID userId;

    @Column
    @Comment("업체명")
    private String companyName;

    @Column
    @Comment("업체타입")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Column
    @Comment("주소")
    private String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();


    public static Company create(String companyName, UUID hubId, CompanyType companyType, String address, UUID companyUserId, UUID userId) {
        Company company = new Company();
        company.companyName = companyName;
        company.hubId = hubId;
        company.userId = companyUserId;
        company.companyType = companyType;
        company.address = address;
        company.createdBy = String.valueOf(userId);
        return company;
    }

    public Product getProductById(UUID productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 업체에 상품이 없습니다."));
    }

    public void updateCompany(String companyName, UUID hubId, String address, UUID userId) {
        if (companyName != null) this.companyName = companyName;
        if (hubId != null) this.hubId = hubId;
        if (address != null) this.address = address;
        this.updatedBy = String.valueOf(userId);
    }

    public void deletedCompany(UUID userId) {
        delete(userId);
    }

    // 상품 등록 메서드
    public Product createProduct(String productName, Integer price, Integer stock, UUID userId) {
        // 정적 팩토리 메서드 호출하여 Product 생성
        Product product = Product.create(productName, price, stock, userId, this); // Company 객체를 참조

        // 회사에 상품 추가
        this.products.add(product);

        return product;
    }

    public void removeProductByProductId(UUID productId) {
        Product product = getProductById(productId);
        products.remove(product);
    }
}
