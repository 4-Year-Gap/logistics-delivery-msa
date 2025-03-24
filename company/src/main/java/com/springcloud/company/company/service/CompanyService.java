package com.springcloud.company.company.service;

import com.springcloud.company.common.IdentityIntegrationResponse;
import com.springcloud.company.common.UserRole;
import com.springcloud.company.company.dto.*;
import com.springcloud.company.company.entity.Company;
import com.springcloud.company.company.infrastructure.external.IdentityIntegrationEventPublisher;
import com.springcloud.company.company.repository.CompanyRepository;
import com.springcloud.company.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final HubClient hubClient;

    private final CompanyRepository companyRepository;

    private final IdentityIntegrationEventPublisher eventPublisher;

    private final RedisTemplate<String, IdentityIntegrationResponse> redisTemplate;

    //존재하는 허브Id인지 확인
    public Integer verifiedHubInfo(UUID hubId){
        return hubClient.verifiedHub(hubId);
    }

    //유저 권한 확인 메서드 -> 래디스로 요청하여 userId에 해당하는 허브아이디, 배송아이디, 업체아이디 확인 가능하다.
    private IdentityIntegrationResponse getIdentityIntegrationCache(UUID userId) {
        HashOperations<String, String, IdentityIntegrationResponse> hashOps = redisTemplate.opsForHash();

        IdentityIntegrationResponse identityIntegrationCache = hashOps.get("identityIntegrationCache", userId.toString());

        if (null == identityIntegrationCache){
            throw new IllegalArgumentException("레디스에 존재 하지 않음");
        }

        return identityIntegrationCache;
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

    // 업체 등록 기능
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto, UUID userId, UserRole userRole) {
        // MASTER 또는 HUB_MANAGER만 업체 생성 가능
        if (userRole != UserRole.MASTER && userRole != UserRole.HUB_MANAGER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // HUB_MANAGER인 경우 허브 검증
        if (userRole == UserRole.HUB_MANAGER) {
            verifyHubAccess(userId, requestDto.getHubId());
        }

        // 검증 통과 후 업체 생성
        return createCompanyAndPublishEvent(requestDto, userId);
    }

    // 공통 로직 분리 (업체 생성 + Kafka 이벤트 발행)
    private CompanyResponseDto createCompanyAndPublishEvent(CompanyRequestDto requestDto, UUID userId) {
//        // 존재하는 허브인지 확인
//        Integer hub = verifiedHubInfo(requestDto.getHubId());
//        if (hub != 1) {
//            throw new IllegalArgumentException("존재하는 HubId가 아닙니다.");
//        }

        // 업체 생성
        Company company = Company.create(
                requestDto.getCompanyName(),
                requestDto.getHubId(),
                requestDto.getCompanyType(),
                requestDto.getAddress(),
                requestDto.getUserId(),
                userId
        );

        Company insertCompany = companyRepository.save(company);

        // Kafka 이벤트 발행
        if (insertCompany.getUserId() != null && insertCompany.getId() != null) {
            CreateIdentityIntegrationCommand integrationCommand = CreateIdentityIntegrationCommand.fromEntity(insertCompany);
            eventPublisher.publish(integrationCommand);
        }

        return new CompanyResponseDto(company);
    }

    // 주문 서버 요청 로직
    public OrderProductResponseDto readOrderProduct(UUID receivingCompanyId, UUID productId, Integer quantity) {
        //공급 업체 조회하기
        Company supplierCompany = companyRepository.findByProducts_Id(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 가진 공급 업체가 없습니다."));

        //업체를 통해 상품 조회 후 재고 체크(애그리거트 루트로 접근)
        Product product = supplierCompany.getProductById(productId);

        //재고 체크
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }

        // 4. 수령 업체 조회 (예외 처리 추가)
        Company receivingCompany = companyRepository.findById(receivingCompanyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 수령 업체를 찾을 수 없습니다."));


        return new OrderProductResponseDto(supplierCompany.getHubId(), product.getId(), receivingCompany.getHubId());
    }

    @Transactional
    // 업체 정보 수정
    public CompanyResponseDto updateCompany(UpdateCompanyRequestDto requestDto, UUID companyId, UUID userId, UserRole userRole) {
        //권한 확인(마스터, 허브, 업체 담당자만 수정 가능)
        if (userRole != UserRole.MASTER && userRole != UserRole.HUB_MANAGER && userRole != UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // HUB_MANAGER인 경우 허브 검증
        if (userRole == UserRole.HUB_MANAGER) {
            verifyHubAccess(userId, requestDto.getHubId());
        }

        // 업체의 업체 담당자인지 확인 - userId와 JWT userID 일치하는지 확인
        if (userRole == UserRole.COMPANY_MANAGER) {
            verifyCompanyAccess(userId, companyId);
        }

//        // 존재하는 허브인지 확인_변경할 허브가 존재하는지 확인하기
//        Integer hub = verifiedHubInfo(requestDto.getHubId());
//        if (hub != 1) {
//            throw new IllegalArgumentException("존재하는 HubId가 아닙니다.");
//        }

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new NoSuchElementException("등록한 업체가 존재하지 않습니다."));

        //업체 엔티티 수정
        company.updateCompany(requestDto.getCompanyName(), requestDto.getHubId(), requestDto.getAddress(), userId);

        //kafka 이벤트 큐 보내기
        if(company.getUserId() != null && company.getId() != null){
            UpdateIdentityIntegrationCommand integrationCommand = UpdateIdentityIntegrationCommand.fromEntity(company);
            eventPublisher.publish(integrationCommand);
        }

        return new CompanyResponseDto(company);
    }

    // 업체 전체 조회
    public Page<CompanyResponseDto> getAllCompany(Integer page, Integer size, String sortBy, String sortDirection, String keyword) {

        //정렬 방향 결정
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // 검색어가 있는 경우 처리
        if (keyword != null && !keyword.isEmpty()) {
            return companyRepository.searchCompanies(keyword, pageable)
                    .map(CompanyResponseDto::new);
        } else {
            return companyRepository.findAll(pageable)
                    .map(CompanyResponseDto::new);
        }
    }

    //업체 단일 조회
    public CompanyResponseDto getCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        return new CompanyResponseDto(company);
    }

    // 업체 삭제
    @Transactional
    public void deleteCompany(UUID companyId, UUID userId, UserRole userRole) {
        //권한 확인(마스터, 허브, 업체 담당자만 수정 가능)
        if (userRole != UserRole.MASTER && userRole != UserRole.HUB_MANAGER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new NoSuchElementException("해당 업체가 존재하지 않습니다."));

        // HUB_MANAGER인 경우 허브 검증
        if (userRole == UserRole.HUB_MANAGER) {

            verifyHubAccess(userId, company.getHubId());
        }

        company.deletedCompany(userId);

        //kafka 이벤트 큐 보내기
        if(company.getUserId() != null && company.getId() != null){
            DeleteIdentityIntegrationCommand integrationCommand = DeleteIdentityIntegrationCommand.fromEntity(company);
            eventPublisher.publish(integrationCommand);
        }
    }

    //상품 ID로 업체 조회
    public Company getCompanyByProductId(UUID productId) {
        return companyRepository.findByProducts_Id(productId).orElseThrow();
    }

}
