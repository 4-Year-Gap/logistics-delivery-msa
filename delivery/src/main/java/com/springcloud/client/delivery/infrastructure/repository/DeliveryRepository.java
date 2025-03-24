package com.springcloud.client.delivery.infrastructure.repository;

import com.springcloud.client.delivery.domain.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID>,CustomDeliveryRepository {
    @Override
    Optional<Delivery> findById(UUID uuid);

    Delivery findByOrderId(UUID orderId);
}