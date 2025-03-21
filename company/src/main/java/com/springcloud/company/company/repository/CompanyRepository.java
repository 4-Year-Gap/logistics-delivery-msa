package com.springcloud.company.company.repository;

import com.springcloud.company.company.entity.Company;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByUserId(UUID userId);

    @EntityGraph(attributePaths = {"products"})
    Optional<Company> findByProducts_Id(UUID productId);
}
