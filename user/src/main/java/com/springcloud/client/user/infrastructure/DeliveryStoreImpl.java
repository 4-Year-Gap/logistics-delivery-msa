package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.DeliveryAssignment;
import com.springcloud.client.user.domain.DeliveryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryStoreImpl implements DeliveryStore {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;

    @Override
    public void save(DeliveryAssignment assignment) {
        deliveryAssignmentRepository.save(assignment);
    }
}
