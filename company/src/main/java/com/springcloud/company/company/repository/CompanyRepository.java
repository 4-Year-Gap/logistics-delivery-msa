package com.springcloud.company.company.repository;

import com.springcloud.company.company.entity.Company;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @EntityGraph(attributePaths = {"products"})
    Optional<Company> findByProducts_Id(UUID productId);

    Optional<Company> findByUserId(UUID userId);
}
