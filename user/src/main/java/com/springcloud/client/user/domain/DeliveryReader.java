package com.springcloud.client.user.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryReader {

    Integer findMaxDeliveryOrderNumberByRole(DeliveryDriverRole deliveryDriverRole);

    List<DeliveryDriver> findAllByRoleOrderByDeliveryOrderNumberAsc(DeliveryDriverRole role);

    DeliveryAssignment findWithLock(DeliveryDriverRole driverType);

    Optional<DeliveryDriver> findById(UUID userId);

    List<DeliveryDriver> findAllByHubIdAndRoleOrderByDeliveryOrderNumberAsc(UUID hubId, DeliveryDriverRole role);

    Page<DeliveryDriver> findAll(Pageable pageable);

    Page<DeliveryDriver> findByUser_UsernameContainingAndDeletedAtIsNull(String keyword, Pageable pageable);
}