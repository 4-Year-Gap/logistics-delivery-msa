package com.springcloud.client.delivery.infrastructure.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomDeliveryHubRouteRepository {
    Optional<UUID> findByOrderIdAndStartHubOrDestinationHub(UUID orderId, UUID hubId);

    Optional<UUID> findByOrderIdAndSearchDeliver(UUID orderId, UUID userId);

    Optional<List<UUID>> findByUserIdSearchDeliver(UUID userId);

    Optional<List<UUID>> findByHubIdSearchDeliver(UUID hubId);
}
