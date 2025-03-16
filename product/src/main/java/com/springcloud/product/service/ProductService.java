package com.springcloud.product.service;

import com.springcloud.product.dto.ProductRequestDto;
import com.springcloud.product.dto.ProductResponseDto;
import com.springcloud.product.entity.Product;
import com.springcloud.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        //객체 생성
        Product product = Product.create(
                requestDto.getProductName(),
                requestDto.getPrice(),
                requestDto.getStock()
        );
        productRepository.save(product);
        return new ProductResponseDto(product);
    }
}
