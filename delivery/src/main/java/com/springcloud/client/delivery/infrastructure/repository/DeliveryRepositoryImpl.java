package com.springcloud.client.delivery.infrastructure.repository;


import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.client.delivery.domain.delivery.Delivery;
import com.springcloud.client.delivery.domain.delivery.QDelivery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.springcloud.client.delivery.domain.delivery.QDelivery.delivery;

@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements CustomDeliveryRepository{


    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Delivery> search(Integer userId, String role, Pageable pageable) {

        List<Delivery> query = queryFactory.
                select(delivery)
                .from(delivery)
                .fetch();

        //조건 추가

        JPQLQuery<Delivery> count = queryFactory
                .selectFrom(delivery)
                .from(delivery);

        return  PageableExecutionUtils.getPage(query, pageable, count::fetchCount);
    }
}
