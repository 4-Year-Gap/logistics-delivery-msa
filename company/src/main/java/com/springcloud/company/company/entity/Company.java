package com.springcloud.company.company.entity;

import com.springcloud.company.product.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;

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

    public static Company create(String companyName, UUID hubId, CompanyType companyType, String address, UUID userId) {
        Company company = new Company();
        company.companyName = companyName;
        company.hubId = hubId;
        company.userId = userId;
        company.companyType = companyType;
        company.address = address;
        return company;
    }
}
