package com.springcloud.company.company.dto;

import com.springcloud.company.company.entity.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class UpdateCompanyRequestDto {
    //업체명
    private String companyName;
    //허브ID
    private UUID hubId;
    //주소
    private String address;
}
