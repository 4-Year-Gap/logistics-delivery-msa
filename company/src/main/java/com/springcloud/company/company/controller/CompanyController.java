package com.springcloud.company.company.controller;

import com.springcloud.company.company.dto.*;
import com.springcloud.company.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    //주문 서버 요청 -> 카프카로 요청
    @PostMapping("/check")
    private OrderProductResponseDto getCompanyProductOrder(@RequestBody OrderProductRequestDto requestDto)
    {
        UUID receivingCompanyId = requestDto.getReceivingCompanyId();
        UUID productId = requestDto.getProductId();
        Integer quantity = requestDto.getQuantity();

        return companyService.readOrderProduct(receivingCompanyId, productId, quantity);
    }

    @Description("업체 수정")
    @PatchMapping
    private CompanyResponseDto updateCompany(
            @RequestBody UpdateCompanyRequestDto companyRequestDto
            //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ){
        UUID userId = UUID.fromString("e7fb3090-17e9-4c26-8914-f224a2fc2074"); // TODO: 임시 데이터 튼튼타이어 -> 강철타이어
        return companyService.updateCompany(companyRequestDto, userId);

    }

    @Description("업체 조회")
    @GetMapping("/all")
    private List<CompanyResponseDto> getAllCompanies() {
        return companyService.getAllCompany();
    }

    @Description("업체 삭제")
    @DeleteMapping("/{companyId}")
    private void deleteCompany(@PathVariable UUID companyId
                               //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ){
        UUID userId = UUID.randomUUID(); // TODO: 임시 데이터
        companyService.deleteCompany(companyId, userId);
    }


}
