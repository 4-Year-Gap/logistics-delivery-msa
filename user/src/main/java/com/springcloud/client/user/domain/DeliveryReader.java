package com.springcloud.client.user.domain;

import java.util.List;

public interface DeliveryReader {

    List<DeliveryDriver> findAllByRoleOrderByDeliveryOrderNumberAsc(DeliveryDriverRole role);

    DeliveryAssignment findWithLock(int pk);
}