package com.springcloud.company.product.controller;

import com.springcloud.company.product.dto.*;
import com.springcloud.company.product.infrastructure.dto.OrderCreateEvent;
import com.springcloud.company.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Description("상품 등록")
    @PostMapping
    public ProductResponseDto createProduct(
            @RequestBody ProductRequestDto requestDto
            //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ){
        UUID userId = UUID.fromString("3bf9a1e6-f9d9-494d-af81-891e151e5a39"); //UUID.randomUUID();  TODO: 임시 데이터
        //응답 보내기
        return productService.createProduct(requestDto,userId);
    }

    @Description("상품 수정(재고 포함)")
    @PatchMapping("/{productId}")
    public ProductResponseDto updateProductStock(@PathVariable UUID productId, @RequestBody UpdateProductRequestDto RequestDto
                                                 //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ) {
        UUID userId = UUID.fromString("77daaade-593b-4284-8416-b82570e1ce4f");
        return productService.updateProduct(productId,RequestDto,userId);
    }

    @Description("상품 전체 조회")
    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @Description("상품 상세 조회")
    @GetMapping("/{productId}")
    public ProductResponseDto getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    @Description("등록 상품 전체 조회")
    @GetMapping("/me")
    public List<ProductResponseDto> getProducts(
            //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ) {
        UUID userId = UUID.fromString("77daaade-593b-4284-8416-b82570e1ce4f");
        return productService.getProducts(userId);
    }

    @Description("등록 상품 삭제")
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable UUID productId
            //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ) {
        UUID userId = UUID.fromString("3bf9a1e6-f9d9-494d-af81-891e151e5a39");
        productService.deleteProduct(productId,userId);
    }

    @PostMapping("/stock/test")
    public ResponseEntity<String> updateStock(@RequestBody OrderCreateEvent orderCreateEvent) {
        try {
            productService.updateStockTest(orderCreateEvent);
            return ResponseEntity.ok("Stock updated successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
