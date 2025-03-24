package com.springcloud.client.delivery.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.client.delivery.domain.delivery.QDelivery;
import com.springcloud.client.delivery.domain.delivery.QDeliveryHubRoute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.springcloud.client.delivery.domain.delivery.QDelivery.delivery;
import static com.springcloud.client.delivery.domain.delivery.QDeliveryHubRoute.deliveryHubRoute;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DeliveryHubRouteRepositoryImpl implements CustomDeliveryHubRouteRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<UUID> findByOrderIdAndStartHubOrDestinationHub(UUID searchOrderId, UUID hubId) {

        QDelivery delivery = QDelivery.delivery;
        QDeliveryHubRoute deliveryHubRoute = QDeliveryHubRoute.deliveryHubRoute;

        UUID foundOrderId = queryFactory
                .select(delivery.orderId)
                .from(delivery)
                .join(delivery.deliveryHubRouteList, deliveryHubRoute)
                .where(
                        deliveryHubRoute.startHub.eq(hubId)
                                .or(deliveryHubRoute.destinationHub.eq(hubId)),
                        delivery.orderId.eq(searchOrderId)
                )
                .distinct()
                .fetchOne();

        if (foundOrderId == null) {
            log.warn("주문 ID를 찾을 수 없습니다. hubId: {}, searchOrderId: {}", hubId, searchOrderId);
            return Optional.empty();
        }

        log.info("주문 ID 조회 성공. hubId: {}, searchOrderId: {}, foundOrderId: {}", hubId, searchOrderId, foundOrderId);
        return Optional.of(foundOrderId);
    }

    @Override
    public Optional<UUID> findByOrderIdAndSearchDeliver(UUID orderId, UUID userId) {
        UUID foundOrderId = queryFactory
                .select(delivery.orderId)
                .from(delivery)
                .join(delivery.deliveryHubRouteList, deliveryHubRoute)
                .where(
                        delivery.companyDeliver.eq(userId)
                                .or(deliveryHubRoute.shipperId.eq(userId)),
                        delivery.orderId.eq(orderId)
                )
                .distinct()
                .fetchOne();

        if (foundOrderId == null) {
            log.warn("주문 ID를 찾을 수 없습니다. userId: {}, orderId: {}", userId, orderId);
            return Optional.empty();
        }

        log.info("주문 ID 조회 성공. userId: {}, orderId: {}, foundOrderId: {}", userId, orderId, foundOrderId);
        return Optional.of(foundOrderId);
    }

    @Override
    public Optional<List<UUID>> findByUserIdSearchDeliver(UUID userId) {

        List<UUID> foundOrderIds = queryFactory
                .select(delivery.orderId)
                .from(delivery)
                .join(delivery.deliveryHubRouteList, deliveryHubRoute)
                .where(
                        delivery.companyDeliver.eq(userId).or(deliveryHubRoute.shipperId.eq(userId))
                )
                .distinct()
                .fetch();

        if (foundOrderIds == null) {
            log.warn("주문 ID를 찾을 수 없습니다. userId: {}", userId);
            return Optional.empty();
        }

        log.info("주문 ID 조회 성공. userId: {}, foundOrderId: {}", userId, foundOrderIds);
        return Optional.of(foundOrderIds);
    }

    @Override
    public Optional<List<UUID>> findByHubIdSearchDeliver(UUID hubId) {
        List<UUID> foundOrderIdList = queryFactory
                .select(delivery.orderId)
                .from(delivery)
                .join(delivery.deliveryHubRouteList, deliveryHubRoute)
                .where(
                        deliveryHubRoute.startHub.eq(hubId).or(deliveryHubRoute.destinationHub.eq(hubId))
                )
                .distinct()
                .fetch();

        if (foundOrderIdList == null) {
            log.warn("주문 ID를 찾을 수 없습니다. hubId: {}", hubId);
            return Optional.empty();
        }

        log.info("주문 ID 조회 성공. hubId: {}, foundOrderId: {}", hubId, foundOrderIdList);
        return Optional.of(foundOrderIdList);
    }
}
