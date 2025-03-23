package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.DeliveryAssignment;
import com.springcloud.client.user.domain.DeliveryDriverRole;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT da FROM DeliveryAssignment da WHERE da.driverType = :driverType")
    DeliveryAssignment findWithLock(DeliveryDriverRole driverType);
}
