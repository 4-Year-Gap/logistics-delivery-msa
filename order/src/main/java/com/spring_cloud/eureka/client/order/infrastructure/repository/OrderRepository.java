package com.spring_cloud.eureka.client.order.infrastructure.repository;

import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> , OrderCustomRepository {

    OrderEntity findByOrderIdAndDeletedAtIsNull(UUID orderId);


    OrderEntity findByOrderIdAndConsumeCompanyIdOrSupplyCompanyId(UUID orderId, UUID companyId,UUID companyId2);

    OrderEntity findByOrderId(UUID orderId);

    Page<OrderEntity> findAllByConsumeCompanyIdOrSupplyCompanyId(UUID consumeCompanyId,UUID id, Pageable pageable);


}
