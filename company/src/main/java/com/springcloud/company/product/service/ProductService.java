package com.springcloud.company.product.service;

import com.springcloud.company.common.IdentityIntegrationResponse;
import com.springcloud.company.common.UserRole;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, IdentityIntegrationResponse> redisTemplate;

    //유저 권한 확인 메서드 -> 래디스로 요청하여 userId에 해당하는 허브아이디, 배송아이디, 업체아이디 확인 가능하다.
    private IdentityIntegrationResponse getIdentityIntegrationCache(UUID userId) {
        HashOperations<String, String, IdentityIntegrationResponse> hashOps = redisTemplate.opsForHash();

        IdentityIntegrationResponse identityIntegrationCache = hashOps.get("identityIntegrationCache", userId.toString());

        if (null == identityIntegrationCache){
            throw new IllegalArgumentException("레디스에 존재 하지 않음");
        }

        return identityIntegrationCache;
    }

    //상품 등록
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto, UUID userId, UserRole userRole) {
        //권한 확인(마스터, 허브, 업체 담당자만 등록 가능)
        if (userRole != UserRole.MASTER && userRole != UserRole.HUB_MANAGER && userRole != UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        //company ID를 통해 업체 조회
        Company company = companyRepository.findById(requestDto.getCompanyId())
                .orElseThrow(()-> new IllegalArgumentException("해당 업체를 찾을 수 없습니다."));

        // HUB_MANAGER인 경우 허브 검증
        if (userRole == UserRole.HUB_MANAGER) {
            verifyHubAccess(userId, company.getHubId());
        }

        // 업체의 업체 담당자인지 확인 - userId와 JWT userID 일치하는지 확인
        if (userRole == UserRole.COMPANY_MANAGER) {
            verifyCompanyAccess(userId, company.getId());
        }

        //객체 생성
        Product product = company.createProduct(
                requestDto.getProductName(),
                requestDto.getPrice(),
                requestDto.getStock(),
                userId
        );

        return new ProductResponseDto(product);
    }

    // 허브 접근 권한 체크
    private void verifyHubAccess(UUID userId, UUID hubId) {
        IdentityIntegrationResponse identityIntegrationResponse = getIdentityIntegrationCache(userId);
        UUID managerHubId = identityIntegrationResponse.getHubId(); // 허브 관리자 권한의 허브 ID

        if (!managerHubId.equals(hubId)) {
            throw new IllegalArgumentException("해당 허브의 업체만 접근할 수 있습니다.");
        }
    }

    // 업체 접근 권한 체크
    private void verifyCompanyAccess(UUID userId, UUID companyId) {
        IdentityIntegrationResponse identityIntegrationResponse = getIdentityIntegrationCache(userId);
        UUID managerCompanyId = identityIntegrationResponse.getCompanyId(); // 허브 관리자 권한의 허브 ID

        if (!managerCompanyId.equals(companyId)) {
            throw new IllegalArgumentException("해당 유저의 담당 업체만 접근할 수 있습니다.");
        }
    }

    //상품 수정_주문 -> 재고 차감 로직 메서드
    @Transactional
    public void updateStock(OrderCreateEvent orderCreateEvent) {

        Product product = productRockRepository.findByIdWithLock(orderCreateEvent.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        // 상품 도메인 재고차감 로직
        product.updateQuantity(orderCreateEvent.getProductQuantity());
    }

    @Transactional
    //상품 수정_업체 -> 상품 수정
    public ProductResponseDto updateProduct(UUID productId, UpdateProductRequestDto requestDto, UUID userId, UserRole userRole) {
        //권한 확인(마스터, 허브, 업체 담당자만 등록 가능)
        if (userRole != UserRole.MASTER && userRole != UserRole.HUB_MANAGER && userRole != UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Product product = productRockRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        Company company = product.getCompany();

        // HUB_MANAGER인 경우 허브 검증
        if (userRole == UserRole.HUB_MANAGER) {
            verifyHubAccess(userId, company.getHubId());
        }

        // 업체의 업체 담당자인지 확인 - userId와 JWT userID 일치하는지 확인
        if (userRole == UserRole.COMPANY_MANAGER) {
            verifyCompanyAccess(userId, company.getId());
        }

        product.updateProduct(requestDto.getProductName(),requestDto.getProductPrice(),requestDto.getQuantity(),userId);

        return new ProductResponseDto(product);
    }

    // 전체 상품 조회_ 허브담당자는 담당 허브만 접근 가능
    public Page<ProductResponseDto> getAllProducts(String keyword, UUID userId, UserRole userRole, Pageable pageable) {
        Page<Product> productPage;

        // HUB_MANAGER인 경우 담당 허브 검색
        if (userRole == UserRole.HUB_MANAGER) {
            IdentityIntegrationResponse identityIntegrationResponse = getIdentityIntegrationCache(userId);
            UUID hubId = identityIntegrationResponse.getHubId();

            // 허브 ID에 해당하는 업체들 조회
            List<Company> companies = companyRepository.findByHubId(hubId);

            // 해당 업체들의 ID 리스트 추출
            List<UUID> companyIds = companies.stream()
                    .map(Company::getId)
                    .toList();

            // 업체 ID 리스트에 속하는 상품만 조회 + 키워드 필터링
            productPage = productRepository.findByCompanyIdInAndProductNameContaining(companyIds, keyword, pageable);
        } else {
            // 전체 업체에서 상품 조회 + 키워드 필터링
            productPage = productRepository.findByProductNameContaining(keyword, pageable);
        }

        return productPage.map(ProductResponseDto::new);
    }

    //상품 상세 조회
    public ProductResponseDto getProduct(UUID productId) {
        Company company = companyService.getCompanyByProductId(productId);

        Product product = company.getProducts().stream()
                .filter( p -> p.getId().equals(productId))
                .findAny()
                .orElseThrow();

        return new ProductResponseDto(product);
    }

    //업체담당자가 등록했던 상품들 조회_권한 설정 필요
    public List<ProductResponseDto> getProducts(UUID userId, String keyword) {
        //업체 담당자인지 확인 절차가 포함됨
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저 ID에 대한 회사 정보가 없습니다."));

        List<Product> productList = company.getProducts();

        // keyword가 존재할 경우 필터링 수행
        if (keyword != null && !keyword.trim().isEmpty()) {
            productList = productList.stream()
                    .filter(product -> product.getProductName().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }

        return productList.stream()
                .map(ProductResponseDto::new)
                .toList();


    }
    @Transactional
    public void deleteProduct(UUID productId, UUID userId, UserRole userRole) {
        Company company = companyService.getCompanyByProductId(productId);
        
        //권한 확인(마스터, 허브관리자만 삭제 가능)
        if (userRole != UserRole.MASTER && userRole != UserRole.HUB_MANAGER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        
        // HUB_MANAGER인 경우 허브 검증
        if (userRole == UserRole.HUB_MANAGER) {
            verifyHubAccess(userId, company.getHubId());
        }

        Product product = company.getProducts().stream()
                .filter( p -> p.getId().equals(productId))
                .findAny()
                .orElseThrow();

        product.deleteProduct(userId);
    }
}
