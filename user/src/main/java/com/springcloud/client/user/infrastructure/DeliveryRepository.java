package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.DeliveryDriver;
import com.springcloud.client.user.domain.DeliveryDriverRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<DeliveryDriver, UUID> {

    // Role이 HUB인 배송 기사들 중 최대 deliveryOrderNumber 조회
    @Query("SELECT MAX(dd.deliveryOrderNumber) FROM DeliveryDriver dd WHERE dd.role = :role")
    Integer findMaxDeliveryOrderNumberByRole(@Param("role") DeliveryDriverRole role);

    // Role이 HUB인 배송 기사들을 deliveryOrderNumber 순으로 조회
    List<DeliveryDriver> findAllByRoleOrderByDeliveryOrderNumberAsc(DeliveryDriverRole role);

    List<DeliveryDriver> findAllByHubIdAndRoleOrderByDeliveryOrderNumberAsc(UUID hubId, DeliveryDriverRole role);

    Page<DeliveryDriver> findByUser_UsernameContainingAndDeletedAtIsNull(String keyword, Pageable pageable);
}