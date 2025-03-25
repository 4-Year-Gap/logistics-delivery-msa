package com.springcloud.company.product.repository;

import com.springcloud.company.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR p.productName LIKE %:keyword%)")
    List<Product> searchProducts(String keyword);

    // 특정 업체(들)에 속한 상품 조회 (허브 담당자용)
    Page<Product> findByCompanyIdInAndProductNameContaining(List<UUID> companyIds, String keyword, Pageable pageable);

    // 모든 업체에서 상품 조회 (일반 사용자용)
    Page<Product> findByProductNameContaining(String keyword, Pageable pageable);
}

