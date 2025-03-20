package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.DeliveryDriver;
import com.springcloud.client.user.domain.DeliveryDriverRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<DeliveryDriver, UUID> {

    List<DeliveryDriver> findAllByRoleOrderByDeliveryOrderNumberAsc(DeliveryDriverRole role);
}