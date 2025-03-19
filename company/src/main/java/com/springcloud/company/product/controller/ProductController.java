package com.springcloud.company.product.controller;

import com.springcloud.company.product.dto.ProductRequestDto;
import com.springcloud.company.product.dto.ProductResponseDto;
import com.springcloud.company.product.dto.UpdateProductStockRequestDto;
import com.springcloud.company.product.dto.UpdateProductStockResponseDto;
import com.springcloud.company.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponseDto createProduct(
            @RequestBody ProductRequestDto requestDto
            //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ){
        UUID userId = UUID.fromString("1512a8fe-f5c3-4ac8-998e-84c2fc2bab7d"); //UUID.randomUUID();  TODO: 임시 데이터
        //응답 보내기
        return productService.createProduct(requestDto,userId);
    }
    @GetMapping("/stock")
    @Description("상품 재고 차감")
    public UpdateProductStockResponseDto updateProductStock(@RequestBody UpdateProductStockRequestDto RequestDto
    ){
        return productService.updateStock(RequestDto);
    }
}
