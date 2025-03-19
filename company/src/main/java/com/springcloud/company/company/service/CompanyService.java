package com.springcloud.company.company.service;

import com.springcloud.company.company.dto.CompanyRequestDto;
import com.springcloud.company.company.dto.CompanyResponseDto;
import com.springcloud.company.company.entity.Company;
import com.springcloud.company.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    public CompanyResponseDto createCompany(CompanyRequestDto RequestDto, UUID userId) {
        Company company = Company.create(
                RequestDto.getCompanyName(),
                RequestDto.getHubId(),
                RequestDto.getCompanyType(),
                RequestDto.getAddress(),
                userId
        );
        companyRepository.save(company);
        return new CompanyResponseDto(company);
    }

}
