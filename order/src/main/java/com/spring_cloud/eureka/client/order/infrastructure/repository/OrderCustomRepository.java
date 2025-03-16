package com.spring_cloud.eureka.client.order.infrastructure.repository;

import com.spring_cloud.eureka.client.order.application.dto.OrderSearchCondition;
import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCustomRepository {
//    Page<OrderEntity> search(OrderSearchCondition searchCondition, Pageable pageable);
}
