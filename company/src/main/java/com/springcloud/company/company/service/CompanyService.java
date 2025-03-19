package com.springcloud.company.company.service;

import com.springcloud.company.company.dto.CompanyRequestDto;
import com.springcloud.company.company.dto.CompanyResponseDto;
import com.springcloud.company.company.dto.OrderProductResponseDto;
import com.springcloud.company.company.entity.Company;
import com.springcloud.company.company.repository.CompanyRepository;
import com.springcloud.company.product.entity.Product;
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


    public OrderProductResponseDto readOrderProduct(UUID receivingCompanyId, UUID productId, Integer quantity) {
        //공급 업체 조회하기
        Company supplierCompany =  companyRepository.findByProducts_Id(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 가진 공급 업체가 없습니다."));

        //업체를 통해 상품 조회 후 재고 체크(애그리거트 루트로 접근)
        Product product = supplierCompany.getProductById(productId);

        //재고 체크
        if(quantity > product.getStock()) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }

        // 4. 수령 업체 조회 (예외 처리 추가)
        Company receivingCompany = companyRepository.findById(receivingCompanyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 수령 업체를 찾을 수 없습니다."));


        return new OrderProductResponseDto(supplierCompany.getHubId(), product.getId(), receivingCompany.getHubId());
    }

    //상품 ID로 업체 조회
    public Company getCompanyByProductId(UUID productId){
        return companyRepository.findByProducts_Id(productId).orElseThrow();
    }
}
