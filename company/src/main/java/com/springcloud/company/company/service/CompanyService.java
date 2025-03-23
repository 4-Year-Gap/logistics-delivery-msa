package com.springcloud.company.company.service;

import com.springcloud.company.company.dto.CompanyRequestDto;
import com.springcloud.company.company.dto.CompanyResponseDto;
import com.springcloud.company.company.dto.OrderProductResponseDto;
import com.springcloud.company.company.dto.UpdateCompanyRequestDto;
import com.springcloud.company.company.entity.Company;
import com.springcloud.company.company.repository.CompanyRepository;
import com.springcloud.company.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
        Company supplierCompany = companyRepository.findByProducts_Id(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 가진 공급 업체가 없습니다."));

        //업체를 통해 상품 조회 후 재고 체크(애그리거트 루트로 접근)
        Product product = supplierCompany.getProductById(productId);

        //재고 체크
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }

        // 4. 수령 업체 조회 (예외 처리 추가)
        Company receivingCompany = companyRepository.findById(receivingCompanyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 수령 업체를 찾을 수 없습니다."));


        return new OrderProductResponseDto(supplierCompany.getHubId(), product.getId(), receivingCompany.getHubId());
    }

    @Transactional
    // 업체 정보 수정
    public CompanyResponseDto updateCompany(UpdateCompanyRequestDto requestDto, UUID userId) {
        Company company = companyRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("등록한 업체가 존재하지 않습니다."));

        // 업체의 userId와 JWT userID 일치하는지 확인
        if (!company.getUserId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        //업체 엔티티 수정
        company.updateCompany(requestDto.getCompanyName(), requestDto.getHubId(), requestDto.getAddress(), userId);

        return new CompanyResponseDto(company);
    }

    // 업체 전체 조회
    public List<CompanyResponseDto> getAllCompany(String keyword) {
        List<Company> companyList = companyRepository.searchCompanys(keyword);
        return companyList.stream()
                .map(CompanyResponseDto::new)
                .toList();
    }

    //업체 단일 조회
    public CompanyResponseDto getCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        return new CompanyResponseDto(company);
    }

    // 업체 삭제
    @Transactional
    public void deleteCompany(UUID companyId, UUID userId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new NoSuchElementException("company not found"));
        // 업체의 userId와 JWT userID 일치하는지 확인
        if (!company.getUserId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        company.deletedCompany(userId);
    }

    //상품 ID로 업체 조회
    public Company getCompanyByProductId(UUID productId) {
        return companyRepository.findByProducts_Id(productId).orElseThrow();
    }
}
