package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.DeliveryAssignment;
import com.springcloud.client.user.domain.DeliveryDriver;
import com.springcloud.client.user.domain.DeliveryDriverRole;
import com.springcloud.client.user.domain.DeliveryReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeliveryReaderImpl implements DeliveryReader {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;

    @Override
    public Integer findMaxDeliveryOrderNumberByRole(DeliveryDriverRole role) {
        return deliveryRepository.findMaxDeliveryOrderNumberByRole(role);
    }

    @Override
    public List<DeliveryDriver> findAllByRoleOrderByDeliveryOrderNumberAsc(DeliveryDriverRole role) {
        return deliveryRepository.findAllByRoleOrderByDeliveryOrderNumberAsc(role);
    }

    @Override
    public DeliveryAssignment findWithLock(DeliveryDriverRole driverType) {
        return deliveryAssignmentRepository.findWithLock(driverType);
    }

    @Override
    public Optional<DeliveryDriver> findById(UUID userId) {
        return deliveryRepository.findById(userId);
    }

    @Override
    public List<DeliveryDriver> findAllByHubIdAndRoleOrderByDeliveryOrderNumberAsc(UUID hubId, DeliveryDriverRole role) {
        return deliveryRepository.findAllByHubIdAndRoleOrderByDeliveryOrderNumberAsc(hubId, role);
    }

    @Override
    public Page<DeliveryDriver> findAll(Pageable pageable) {
        return deliveryRepository.findAll(pageable);
    }

    @Override
    public Page<DeliveryDriver> findByUser_UsernameContainingAndDeletedAtIsNull(String keyword, Pageable pageable) {
        return deliveryRepository.findByUser_UsernameContainingAndDeletedAtIsNull(keyword, pageable);
    }
}