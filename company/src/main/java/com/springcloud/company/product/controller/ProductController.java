package com.springcloud.company.product.controller;

import com.springcloud.company.common.UserInfoHeader;
import com.springcloud.company.product.dto.*;
import com.springcloud.company.product.infrastructure.dto.OrderCreateEvent;
import com.springcloud.company.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @RequestBody ProductRequestDto requestDto, HttpServletRequest request
    ){
//        UUID userId = UUID.fromString("3bf9a1e6-f9d9-494d-af81-891e151e5a39"); //UUID.randomUUID();  TODO: 임시 데이터
        UserInfoHeader userInfo = new UserInfoHeader(request);
        //응답 보내기
        return productService.createProduct(requestDto,userInfo.getUserId(), userInfo.getUserRole());
    }

    @Description("상품 수정(재고 포함)")
    @PatchMapping("/{productId}")
    public ProductResponseDto updateProductStock(@PathVariable UUID productId, @RequestBody UpdateProductRequestDto RequestDto,
                                                 HttpServletRequest request
    ) {
        UserInfoHeader userInfo = new UserInfoHeader(request);
        return productService.updateProduct(productId,RequestDto,userInfo.getUserId(),userInfo.getUserRole());
    }

    @Description("상품 전체 조회")
    @GetMapping
    public Page<ProductResponseDto> getAllProducts(@RequestParam(required = false) String keyword,
                                                   HttpServletRequest request,
                                                   @RequestParam(required = false, defaultValue = "1") Integer page,  // 페이지 번호 (기본값 1)
                                                   @RequestParam(required = false, defaultValue = "10") Integer size, // 페이지 크기 (기본값 10)
                                                   @RequestParam(required = false, defaultValue = "createdAt") String sortBy, // 기본 정렬 기준은 생성일 (createdAt)
                                                   @RequestParam(required = false, defaultValue = "asc") String sortDirection
    ){
        UserInfoHeader userInfo = new UserInfoHeader(request);
        //정렬 방향 결정
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return productService.getAllProducts(keyword,userInfo.getUserId(),userInfo.getUserRole(),pageable);
    }


    @Description("상품 상세 조회")
    @GetMapping("/{productId}")
    public ProductResponseDto getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    @Description("등록 상품 전체 조회")
    @GetMapping("/me")
    public List<ProductResponseDto> getProducts(
            @RequestParam(required = false) String keyword
            //, @RequestHeader("X-User-Id") UUID userId -> TODO: 게이트웨이 완성 시 주석 해제
    ) {
        UUID userId = UUID.fromString("77daaade-593b-4284-8416-b82570e1ce4f");
        return productService.getProducts(userId,keyword);
    }

    @Description("등록 상품 삭제")
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable UUID productId,HttpServletRequest request)
    {
        UserInfoHeader userInfo = new UserInfoHeader(request);
        productService.deleteProduct(productId,userInfo.getUserId(),userInfo.getUserRole());
    }

    @PostMapping("/stock/test")
    public ResponseEntity<String> updateStock(@RequestBody OrderCreateEvent orderCreateEvent) {
        try {
            productService.updateStockRedisWithLua(orderCreateEvent);
            return ResponseEntity.ok("Stock updated successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
