package com.spring_cloud.eureka.client.order.infrastructure.repository;



import com.querydsl.jpa.impl.JPAQueryFactory;

import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import com.spring_cloud.eureka.client.order.domain.order.OrderSearchCondition;
import com.spring_cloud.eureka.client.order.domain.order.QOrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.spring_cloud.eureka.client.order.domain.order.QOrderEntity.orderEntity;

@Repository
@RequiredArgsConstructor
public abstract class OrderRepositoryImpl implements OrderCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<OrderEntity>> findAllByOrderIdIn(List<UUID> orderIds, Pageable pageable, OrderSearchCondition orderSearchCondition) {

        List<OrderEntity> result = jpaQueryFactory.selectFrom(QOrderEntity.orderEntity)
                .where(QOrderEntity.orderEntity.orderId.in(orderIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return Optional.ofNullable(result.isEmpty() ? null : result);
    }
}
