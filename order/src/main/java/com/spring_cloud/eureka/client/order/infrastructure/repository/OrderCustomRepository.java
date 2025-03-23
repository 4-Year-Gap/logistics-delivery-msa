package com.spring_cloud.eureka.client.order.infrastructure.repository;

import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import com.spring_cloud.eureka.client.order.domain.order.OrderSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderCustomRepository {
    Optional<List<OrderEntity>> findAllByOrderIdIn(List<UUID> list, Pageable pageable, OrderSearchCondition orderSearchCondition);
}
