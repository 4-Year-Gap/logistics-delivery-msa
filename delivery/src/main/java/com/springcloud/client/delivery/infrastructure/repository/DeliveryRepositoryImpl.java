package com.springcloud.client.delivery.infrastructure.repository;



import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.client.delivery.domain.delivery.Delivery;
import com.springcloud.client.delivery.domain.delivery.QDelivery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements CustomDeliveryRepository{


    private final JPAQueryFactory queryFactory;



    @Override
    public Page<Delivery> search(UUID userId, String role, Pageable pageable,UUID hubId) {
        QDelivery delivery = QDelivery.delivery;

        BooleanExpression whereClause = null;

        switch (role) {
            case "MASTER":
                break;
            case "HUB_MANAGER":
                // 허브 관리자는 담당 허브의 배송 정보만 조회
                whereClause = delivery.endHubId.eq(hubId).or(delivery.startHubId.eq(hubId));
                break;
            case "DELIVERY_PERSON":
                whereClause = delivery.companyDeliver.eq(userId);
                break;
            case "COMPANY_MANAGER":
                whereClause = delivery.receiverId.eq(userId);
                break;
            default:
                return Page.empty();
        }

        QueryResults<Delivery> results = queryFactory
                .selectFrom(delivery)
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
