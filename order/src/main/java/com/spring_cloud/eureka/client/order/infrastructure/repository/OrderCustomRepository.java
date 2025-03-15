package com.spring_cloud.eureka.client.order.infrastructure.repository;

import com.spring_cloud.eureka.client.order.application.OrderSearchCondition;
import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import org.springframework.data.domain.Page;

public interface OrderCustomRepository {
    Page<OrderEntity> search(OrderSearchCondition searchCondition);
}
