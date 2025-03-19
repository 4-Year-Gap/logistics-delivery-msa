package com.springcloud.company.company.controller;

import com.springcloud.company.company.dto.CompanyRequestDto;
import com.springcloud.company.company.dto.CompanyResponseDto;
import com.springcloud.company.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    private CompanyResponseDto createCompany(
            @RequestBody CompanyRequestDto companyRequestDto
            //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
              ){
        UUID userId = UUID.randomUUID(); // TODO: 임시 데이터

        return companyService.createCompany(companyRequestDto, userId);
    }
}
