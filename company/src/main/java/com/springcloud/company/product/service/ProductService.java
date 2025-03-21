package com.springcloud.company.product.service;

import com.springcloud.company.company.entity.Company;
import com.springcloud.company.company.repository.CompanyRepository;
import com.springcloud.company.company.service.CompanyService;
import com.springcloud.company.product.dto.ProductRequestDto;
import com.springcloud.company.product.dto.ProductResponseDto;
import com.springcloud.company.product.dto.UpdateProductStockRequestDto;
import com.springcloud.company.product.dto.UpdateProductStockResponseDto;
import com.springcloud.company.product.entity.Product;
import com.springcloud.company.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;


    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto, UUID userId) {
        //유저 ID를 통해 업체 조회
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저의 업체를 찾을 수 없습니다."));

        //객체 생성
        Product product = Product.create(
                requestDto.getProductName(),
                requestDto.getPrice(),
                requestDto.getStock(),
                userId,
                company
        );
        productRepository.save(product);
        return new ProductResponseDto(product);
    }

    @Transactional
    public UpdateProductStockResponseDto deduckStock(UpdateProductStockRequestDto requestDto) {
        //상품의 ID로 Company불러옴
        Company company = companyService.getCompanyByProductId(requestDto.getProductId());
//
////        // 업체 1: 상품 N 이라서 productId에 맞는 상품을 불러옴
//        Product product = company.getProducts().stream()
//                .filter( p -> p.getId().equals(productId))
//                .findAny()
//                .orElseThrow();
//
        //Optional<Product>를 먼저 저장하면 디버깅과 유지보수가 쉬워짐
        Optional<Product> optionalProduct = company.getProducts().stream()
                .filter(p -> p.getId().equals(requestDto.getProductId()))
                .findAny();

        Product product = optionalProduct.orElseThrow(() -> new NoSuchElementException("Product not found"));

        // 상품 도메인 재고차감 로직
        product.deduct(requestDto.getStock());

        // DB 반영
        productRepository.save(product);

        return new UpdateProductStockResponseDto(product);

    }



}
