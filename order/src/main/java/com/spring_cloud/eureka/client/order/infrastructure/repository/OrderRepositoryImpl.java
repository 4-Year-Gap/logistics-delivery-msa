package com.spring_cloud.eureka.client.order.infrastructure.repository;



import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring_cloud.eureka.client.order.application.OrderSearchCondition;

import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
public abstract class OrderRepositoryImpl implements OrderCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<OrderEntity> search(OrderSearchCondition searchCondition) {


        // 배송 완료 후 구현예정
        // 배송 담당자가 결정이 되어야 검색이 가능

        return null;
    }


}
