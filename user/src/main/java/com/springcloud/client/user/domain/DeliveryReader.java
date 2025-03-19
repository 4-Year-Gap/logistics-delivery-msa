package com.springcloud.client.user.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryReader {

    Integer findMaxDeliveryOrderNumberByRole(DeliveryDriverRole deliveryDriverRole);

    List<DeliveryDriver> findAllByRoleOrderByDeliveryOrderNumberAsc(DeliveryDriverRole role);

    DeliveryAssignment findWithLock(int pk);

    Optional<DeliveryDriver> findById(UUID userId);
}
