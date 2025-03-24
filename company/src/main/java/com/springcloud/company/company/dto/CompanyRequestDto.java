package com.springcloud.company.company.dto;

import com.springcloud.company.company.entity.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CompanyRequestDto {
    //업체명
    private String companyName;
    //허브ID
    private UUID hubId;
    //업체타입
    private CompanyType companyType;
    //주소
    private String address;
    //업체 담당자 배겅_userId
    private UUID userId;
}
