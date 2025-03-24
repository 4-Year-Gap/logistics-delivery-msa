package com.spring_cloud.eureka.client.order.infrastructure.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import com.spring_cloud.eureka.client.order.domain.order.QOrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public abstract class OrderRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<OrderEntity> findAllByOrderIdIn(List<UUID> orderIds, Pageable pageable) {

        QOrderEntity orderEntity = QOrderEntity.orderEntity;

        List<OrderEntity> result = jpaQueryFactory
                .selectFrom(orderEntity)
                .where(orderEntity.orderId.in(orderIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(orderEntity.count())
                .from(orderEntity)
                .where(orderEntity.orderId.in(orderIds))
                .fetchOne();

        log.info("주문 조회 완료. orderIds: {}, pageable: {}, resultSize: {}, total: {}", orderIds, pageable, result.size(), total);
        return new PageImpl<>(result, pageable, total);
    }
}
