package com.springcloud.product.controller;

import com.springcloud.product.dto.ProductRequestDto;
import com.springcloud.product.dto.ProductResponseDto;
import com.springcloud.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto){
        //응답 보내기
        return productService.createProduct(requestDto);
    }
}
