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
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final StockService stockService;
    private final ProductRockRepository productRockRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, IdentityIntegrationResponse> redisTemplate;

    private static final String STOCK_KEY_PREFIX = "product:stock:";

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

    /**
     * 초기화 메서드: 애플리케이션 시작 시 모든 상품의 재고를 Redis에 캐싱
     */
    @PostConstruct
    public void initializeStockCache() {
        List<Product> allProducts = productRepository.findAll();
        for (Product product : allProducts) {
            String stockKey = STOCK_KEY_PREFIX + product.getId();
            stringRedisTemplate.opsForValue().set(stockKey, String.valueOf(product.getStock()));
        }
    }

    /**
     * Redis를 주 스토리지로 사용하는 재고 업데이트 메서드
     * DB는 주기적으로 동기화하거나 장애 복구용으로 사용
     */
    @Transactional
    public void updateStockRedisWithLua(OrderCreateEvent orderCreateEvent) {
        try {
            // Null 체크
            if (orderCreateEvent == null) {
                throw new IllegalArgumentException("OrderCreateEvent 객체가 null입니다.");
            }

            // 수량 Null 체크
            Integer quantity = orderCreateEvent.getProductQuantity();
            if (quantity == null) {
                throw new IllegalArgumentException("상품 수량이 null입니다. 올바른 값을 입력하세요.");
            }

            UUID productId = orderCreateEvent.getProductId();
            String stockKey = STOCK_KEY_PREFIX + productId;

            log.info("OrderCreateEvent: productId={}, quantity={}", productId, quantity);

            // Lua 스크립트 실행
            String script = "...";  // 기존 Lua 스크립트
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
            Long result = redisTemplate.execute(redisScript, Collections.singletonList(stockKey), String.valueOf(quantity));

            log.info("Lua Script Result: {}", result);

            if (result == null) {
                // Redis 값이 없는 경우 DB에서 가져와 초기화 후 다시 실행
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new NoSuchElementException("Product not found"));
                int currentStock = product.getStock();
                stringRedisTemplate.opsForValue().set(stockKey, String.valueOf(currentStock));
                updateStockRedisWithLua(orderCreateEvent);  // 재귀 호출
            } else if (result.equals(Boolean.FALSE)) {
                // 재고 부족 예외 처리
                String currentStockStr = stringRedisTemplate.opsForValue().get(stockKey);
                int currentStock = Integer.parseInt(currentStockStr);
                throw new IllegalArgumentException("재고 부족: 현재 " + currentStock + ", 요청 " + Math.abs(quantity));
            } else {
                // Redis 정상 업데이트 → 비동기 DB 업데이트
                asyncUpdateStock(productId, quantity);
            }
        } catch (Exception e) {
            log.error("예외 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Async
    public void asyncUpdateStock(UUID productId, int newStock) {
        stockService.updateStockInTransaction(productId, newStock);
    }

    /**
     * 주기적으로 모든 Redis 재고 값을 DB에 동기화하는 스케줄러 메서드
     * (예: @Scheduled 어노테이션으로 주기적 실행)
     */
    @Transactional
    public void syncAllStocksToDatabase() {
        Set<String> stockKeys = redisTemplate.keys(STOCK_KEY_PREFIX + "*");
        if (stockKeys == null || stockKeys.isEmpty()) {
            return;
        }

        for (String key : stockKeys) {
            String productIdStr = key.substring(STOCK_KEY_PREFIX.length());
            UUID productId = UUID.fromString(productIdStr);
            String stockStr = stringRedisTemplate.opsForValue().get(key);

            if (stockStr != null) {
                int stock = Integer.parseInt(stockStr);

                try {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new NoSuchElementException("Product not found"));
                    product.setStock(stock);
                    productRepository.save(product);
                } catch (Exception e) {
                    // 로깅 및 오류 처리
                    // 실패한 항목은 재시도 큐에 넣을 수 있음
                }
            }
        }
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

        product.updateProduct(requestDto.getProductName(), requestDto.getProductPrice(), requestDto.getQuantity(), userId);

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
                .filter(p -> p.getId().equals(productId))
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
                .filter(p -> p.getId().equals(productId))
                .findAny()
                .orElseThrow();

        product.deleteProduct(userId);
    }
}
