package com.springcloud.company.product.service;

import com.springcloud.company.company.entity.Company;
import com.springcloud.company.company.repository.CompanyRepository;
import com.springcloud.company.product.dto.ProductRequestDto;
import com.springcloud.company.product.dto.ProductResponseDto;
import com.springcloud.company.product.entity.Product;
import com.springcloud.company.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;


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
}
