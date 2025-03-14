package com.spring_cloud.eureka.client.order.infrastructure.repository;

import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
