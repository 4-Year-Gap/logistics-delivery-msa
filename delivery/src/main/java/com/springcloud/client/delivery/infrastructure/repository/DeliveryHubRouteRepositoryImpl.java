package com.springcloud.client.delivery.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.client.delivery.domain.delivery.QDelivery;
import com.springcloud.client.delivery.domain.delivery.QDeliveryHubRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryHubRouteRepositoryImpl implements CustomDeliveryHubRouteRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<UUID> findByOrderIdAndStartHubOrDestinationHub(UUID searchOrderId, UUID hubId) {

        QDelivery delivery = QDelivery.delivery;
        QDeliveryHubRoute deliveryHubRoute = QDeliveryHubRoute.deliveryHubRoute;

        return Optional.ofNullable(queryFactory
                .select(delivery.orderId)
                .from(delivery)
                .join(delivery.deliveryHubRouteList, deliveryHubRoute)
                .where(
                        deliveryHubRoute.startHub.eq(hubId).or(deliveryHubRoute.destinationHub.eq(hubId)),
                        delivery.orderId.eq(searchOrderId)
                )
                .distinct()
                .fetchOne());
    }

    @Override
    public Optional<UUID> findByOrderIdAndSearchDeliver(UUID orderId, UUID userId) {
        QDelivery delivery = QDelivery.delivery;
        QDeliveryHubRoute deliveryHubRoute = QDeliveryHubRoute.deliveryHubRoute;

        return Optional.ofNullable(queryFactory
                .select(delivery.orderId)
                .from(delivery)
                .join(delivery.deliveryHubRouteList, deliveryHubRoute)

                .where(
                        deliveryHubRoute.shipperId.eq(userId),
                        delivery.orderId.eq(orderId)
                )
                .distinct()
                .fetchOne());
    }
}
