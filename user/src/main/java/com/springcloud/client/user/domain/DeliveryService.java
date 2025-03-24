package com.springcloud.client.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryReader deliveryReader;
    private final DeliveryStore deliveryStore;
    private final UserReader userReader;
    private static final DeliveryDriverRole CURRENT_DRIVER_INDEX_FOR_HUB = DeliveryDriverRole.HUB;
    private static final DeliveryDriverRole CURRENT_DRIVER_INDEX_FOR_COMPANY = DeliveryDriverRole.COMPANY;

    @Transactional
    public DeliveryInfo addHubDeliveryDriver(DeliveryCommand command) {
        User user = userReader.findById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Integer maxOrderNumber = deliveryReader.findMaxDeliveryOrderNumberByRole(DeliveryDriverRole.HUB);
        int newOrderNumber = (maxOrderNumber == null) ? 0 : maxOrderNumber + 1;

        DeliveryDriver deliveryDriver = command.toEntity(user, newOrderNumber);

        DeliveryDriver savedDriver = deliveryStore.save(deliveryDriver);
        return new DeliveryInfo(savedDriver);
    }

    @Transactional
    public DeliveryInfo getHubDeliveryDriver() {
        List<DeliveryDriver> drivers = deliveryReader.findAllByRoleOrderByDeliveryOrderNumberAsc(DeliveryDriverRole.HUB);

        if (drivers.isEmpty()) {
            throw new IllegalStateException("등록된 허브 배송 담당자가 없습니다.");
        }

        DeliveryAssignment assignment = deliveryReader.findWithLock(CURRENT_DRIVER_INDEX_FOR_HUB);

        DeliveryDriver deliveryDriver = drivers.get(assignment.getCurrentDriverIndex());

        int nextDriverIndex = (assignment.getCurrentDriverIndex() + 1) % drivers.size();
        assignment.updateCurrentDriverIndex(nextDriverIndex);
        deliveryStore.save(assignment);

        return new DeliveryInfo(deliveryDriver);
    }

    public DeliveryInfo addCompanyDeliveryDriver(DeliveryCommand command) {
        User user = userReader.findById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Integer maxOrderNumber = deliveryReader.findMaxDeliveryOrderNumberByRole(DeliveryDriverRole.COMPANY);
        int newOrderNumber = (maxOrderNumber == null) ? 0 : maxOrderNumber + 1;

        DeliveryDriver deliveryDriver = command.toEntity(user, newOrderNumber);

        DeliveryDriver savedDriver = deliveryStore.save(deliveryDriver);
        return new DeliveryInfo(savedDriver);
    }

    @Transactional
    public DeliveryInfo getCompanyDeliveryDriver(UUID hubId) {
        List<DeliveryDriver> drivers = deliveryReader.findAllByHubIdAndRoleOrderByDeliveryOrderNumberAsc(hubId, DeliveryDriverRole.COMPANY);
        if (drivers.isEmpty()) {
            throw new IllegalStateException("해당 허브에 등록된 업체 배송 담당자가 없습니다.");
        }

        DeliveryAssignment assignment = deliveryReader.findWithLock(CURRENT_DRIVER_INDEX_FOR_COMPANY);

        DeliveryDriver deliveryDriver = drivers.get(assignment.getCurrentDriverIndex());

        int nextDriverIndex = (assignment.getCurrentDriverIndex() + 1) % drivers.size();
        assignment.updateCurrentDriverIndex(nextDriverIndex);
        deliveryStore.save(assignment);

        return new DeliveryInfo(deliveryDriver);
    }

    public void deleteDeliveryDriver(DeliveryCommand command) {
        DeliveryDriver deliveryDriver = deliveryReader.findById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        deliveryDriver.delete();
        deliveryStore.save(deliveryDriver);
    }
}