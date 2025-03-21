package com.springcloud.company.product.service;

import com.springcloud.company.company.entity.Company;
import com.springcloud.company.company.repository.CompanyRepository;
import com.springcloud.company.company.service.CompanyService;
import com.springcloud.company.product.dto.ProductRequestDto;
import com.springcloud.company.product.dto.ProductResponseDto;
import com.springcloud.company.product.dto.UpdateProductRequestDto;
import com.springcloud.company.product.entity.Product;
import com.springcloud.company.product.infrastructure.dto.OrderCreateEvent;
import com.springcloud.company.product.repository.ProductRepository;
import com.springcloud.company.product.repository.ProductRockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final ProductRockRepository productRockRepository;


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


    //상품 수정_주문 -> 재고 차감 로직 메서드
    @Transactional
    public void updateStock(OrderCreateEvent orderCreateEvent) {

        Product product = productRockRepository.findByIdWithLock(orderCreateEvent.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        // 상품 도메인 재고차감 로직
        product.updateQuantity(orderCreateEvent.getProductQuantity());

//        // DB 반영
//        Product save = productRepository.save(product);
    }

    @Transactional
    //상품 수정_업체 -> 상품 수정
    public ProductResponseDto updateProduct(UUID productId, UpdateProductRequestDto requestDto, UUID userId) {
        Product product = productRockRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        // 업체 담당자인지 확인
        Company company = product.getCompany();
        if (!company.getUserId().equals(userId)) {
            throw new IllegalArgumentException("이 유저는 해당 상품을 삭제할 권한이 없습니다.");
        }

        product.updateProduct(requestDto.getProductName(),requestDto.getProductPrice(),requestDto.getQuantity(),userId);

        return new ProductResponseDto(product);
    }

    // 전체 상품 조회_ 어떤 권한도 접근 가능
    public List<ProductResponseDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();

        return productList.stream()
                .map(ProductResponseDto::new)
                .toList();
    }

    //상품 상세 조회_권한 설정 필요
    public ProductResponseDto getProduct(UUID productId) {
        Company company = companyService.getCompanyByProductId(productId);

        Product product = company.getProducts().stream()
                .filter( p -> p.getId().equals(productId))
                .findAny()
                .orElseThrow();

        return new ProductResponseDto(product);
    }

    //업체담당자가 등록했던 상품들 조회_권한 설정 필요
    public List<ProductResponseDto> getProducts(UUID userId) {
        //업체 담당자인지 확인 절차가 포함됨
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저 ID에 대한 회사 정보가 없습니다."));

        List<Product> productList = company.getProducts();

        return productList.stream()
                .map(ProductResponseDto::new)
                .toList();


    }
    @Transactional
    public void deleteProduct(UUID productId, UUID userId) {
        Company company = companyService.getCompanyByProductId(productId);

        // 업체 담당자인지 확인
        if (!company.getUserId().equals(userId)) {
            throw new IllegalArgumentException("이 유저는 해당 상품을 삭제할 권한이 없습니다.");
        }
        Product product = company.getProducts().stream()
                .filter( p -> p.getId().equals(productId))
                .findAny()
                .orElseThrow();

        product.deleteProduct(userId);
    }
}
