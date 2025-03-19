package com.springcloud.company.company.entity;

import com.springcloud.company.product.entity.BaseEntity;
import com.springcloud.company.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "company")
@NoArgsConstructor
public class Company extends BaseEntity{
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

    public static Company create(String companyName, UUID hubId, CompanyType companyType, String address, UUID userId) {
        Company company = new Company();
        company.companyName = companyName;
        company.hubId = hubId;
        company.userId = userId;
        company.companyType = companyType;
        company.address = address;
        return company;
    }

    public Product getProductById(UUID productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 업체에 상품이 없습니다."));
    }

    public void removeProductByProductId(UUID productId) {
        Product product = getProductById(productId);
        products.remove(product);
    }
}
