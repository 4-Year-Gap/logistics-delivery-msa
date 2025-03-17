package com.springcloud.company.company.dto;

import com.springcloud.company.company.entity.Company;
import com.springcloud.company.company.entity.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDto {
    //업체 ID
    private UUID id;
    //업체명
    private String companyName;
    //허브 ID
    private UUID hubId;
    //유저 ID
    private UUID userId;
    //업체타입
    private CompanyType companyType;
    //주소
    private String address;

    public CompanyResponseDto(Company company) {
        this.id = company.getId();
        this.companyName = company.getCompanyName();
        this.hubId = company.getHubId();
        this.userId = company.getUserId();
        this.companyType = company.getCompanyType();
        this.address = company.getAddress();
    }
}
