package com.springcloud.company.company.controller;

import com.springcloud.company.common.UserInfoHeader;
import com.springcloud.company.company.dto.*;
import com.springcloud.company.company.service.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Description("업체 등록")
    @PostMapping
    private CompanyResponseDto createCompany(
            @RequestBody CompanyRequestDto companyRequestDto,
            HttpServletRequest request
    ){
        UserInfoHeader userInfo = new UserInfoHeader(request);

        return companyService.createCompany(companyRequestDto, userInfo.getUserId(), userInfo.getUserRole());
    }

    @Description("업체 수정")
    @PatchMapping("/{companyId}")
    private CompanyResponseDto updateCompany(
            @RequestBody UpdateCompanyRequestDto companyRequestDto,
            @PathVariable UUID companyId,
            HttpServletRequest request
    ){
        //UUID userId = UUID.fromString("a5c5534b-6a26-436e-a14a-ecb498a30a42"); //TODO: 업체 등록 시 userId
        UserInfoHeader userInfo = new UserInfoHeader(request);
        return companyService.updateCompany(companyRequestDto, companyId, userInfo.getUserId(), userInfo.getUserRole());

    }

    @Description("업체 조회")
    @GetMapping("/all")
    private Page<CompanyResponseDto> getAllCompanies(
            @RequestParam(required = false, defaultValue = "1") Integer page,  // 페이지 번호 (기본값 1)
            @RequestParam(required = false, defaultValue = "10") Integer size, // 페이지 크기 (기본값 10)
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy, // 기본 정렬 기준은 생성일 (createdAt)
            @RequestParam(required = false, defaultValue = "asc") String sortDirection, // 기본 정렬 방향은 오름차순
            @RequestParam(required = false) String keyword // 검색어 (옵션)
    ) {
        return companyService.getAllCompany(page - 1, size, sortBy, sortDirection, keyword);
    }

    @Description("업체 단일 조회")
    @GetMapping("/{companyId}")
    private CompanyResponseDto getCompany(@PathVariable UUID companyId){
        return companyService.getCompany(companyId);
    }

    @Description("업체 삭제")
    @DeleteMapping("/{companyId}")
    private void deleteCompany(@PathVariable UUID companyId,
                               HttpServletRequest request
    ){
//        UUID userId = UUID.fromString("a5c5534b-6a26-436e-a14a-ecb498a30a42"); //TODO: 업체 등록 시 userId
        UserInfoHeader userInfo = new UserInfoHeader(request);
        companyService.deleteCompany(companyId, userInfo.getUserId(), userInfo.getUserRole());
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
