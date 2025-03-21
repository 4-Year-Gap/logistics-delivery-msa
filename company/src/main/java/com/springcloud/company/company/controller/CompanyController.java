package com.springcloud.company.company.controller;

import com.springcloud.company.company.dto.*;
import com.springcloud.company.company.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
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

    @Description("업체 수정")
    @PatchMapping
    private CompanyResponseDto updateCompany(
            @RequestBody UpdateCompanyRequestDto companyRequestDto
            //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ){
        UUID userId = UUID.fromString("a5c5534b-6a26-436e-a14a-ecb498a30a42"); //TODO: 업체 등록 시 userId
        return companyService.updateCompany(companyRequestDto, userId);

    }

    @Description("업체 조회")
    @GetMapping("/all")
    private List<CompanyResponseDto> getAllCompanies() {
        return companyService.getAllCompany();
    }

    @Description("업체 단일 조회")
    @GetMapping("/{companyId}")
    private CompanyResponseDto getCompany(@PathVariable UUID companyId){
        return companyService.getCompany(companyId);
    }

    @Description("업체 삭제")
    @DeleteMapping("/{companyId}")
    private void deleteCompany(@PathVariable UUID companyId
                               //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ){
        UUID userId = UUID.fromString("a5c5534b-6a26-436e-a14a-ecb498a30a42"); //TODO: 업체 등록 시 userId
        companyService.deleteCompany(companyId, userId);
    }

    //주문 서버 요청 - feignClient
    @PostMapping("/check")
    private OrderProductResponseDto getCompanyProductOrder(@RequestBody OrderProductRequestDto requestDto)
    {
        UUID receivingCompanyId = requestDto.getReceivingCompanyId();
        UUID productId = requestDto.getProductId();
        Integer quantity = requestDto.getQuantity();

        return companyService.readOrderProduct(receivingCompanyId, productId, quantity);
    }


}
