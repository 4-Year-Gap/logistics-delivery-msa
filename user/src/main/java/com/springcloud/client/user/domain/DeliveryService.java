package com.springcloud.client.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryReader deliveryReader;
    private final DeliveryStore deliveryStore;
    private static final int PK_FOR_HUB_DELIVERY_DRIVER_INDEX = 1;

    @Transactional
    public DeliveryInfo getHubDeliveryDriver() {
        List<DeliveryDriver> drivers = deliveryReader.findAllByRoleOrderByDeliveryOrderNumberAsc(DeliveryDriverRole.HUB);

        if (drivers.isEmpty()) {
            throw new IllegalStateException("등록된 허브 배송 담당자가 없습니다.");
        }

        DeliveryAssignment assignment = deliveryReader.findWithLock(PK_FOR_HUB_DELIVERY_DRIVER_INDEX);

        DeliveryDriver deliveryDriver = drivers.get(assignment.getCurrentDriverIndex());

        int nextDriverIndex = (assignment.getCurrentDriverIndex() + 1) % drivers.size();
        assignment.updateCurrentDriverIndex(nextDriverIndex);
        deliveryStore.save(assignment);

        return new DeliveryInfo(deliveryDriver);
    }
}
