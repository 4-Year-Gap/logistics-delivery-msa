package com.springcloud.client.user.application;

import com.springcloud.client.user.domain.DeliveryCommand;
import com.springcloud.client.user.domain.DeliveryInfo;
import com.springcloud.client.user.domain.DeliveryService;
import com.springcloud.client.user.domain.UserInfo;
import com.springcloud.client.user.interfaces.UserInfoHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryFacade {

    private final DeliveryService deliveryService;

    public DeliveryInfo registerHubDeliveryDriver(UserInfoHeader userInfoHeader, DeliveryCommand command) {
        return deliveryService.registerHubDeliveryDriver(userInfoHeader, command);
    }

    public DeliveryInfo getHubDeliveryDriver() {
        return deliveryService.getHubDeliveryDriver();
    }

    public DeliveryInfo registerCompanyDeliveryDriver(UserInfoHeader userInfoHeader, DeliveryCommand command) {
        return deliveryService.registerCompanyDeliveryDriver(userInfoHeader, command);
    }

    public DeliveryInfo getCompanyDeliveryDriver(UUID hubId) {
        return deliveryService.getCompanyDeliveryDriver(hubId);
    }

    public void deleteDeliveryDriver(UserInfoHeader userInfoHeader, DeliveryCommand command) {
        deliveryService.deleteDeliveryDriver(userInfoHeader, command);
    }

    public DeliveryInfo updateDeliveryDriver(UserInfoHeader userInfoHeader, DeliveryCommand command) {
        return deliveryService.updateDeliveryDriver(userInfoHeader, command);
    }

    public DeliveryInfo getDeliveryDriver(UserInfoHeader userInfoHeader, UUID deliveryDriverId) {
        return deliveryService.getDeliveryDriver(userInfoHeader, deliveryDriverId);
    }

    public Page<DeliveryInfo> getAllDeliveryDrivers(UserInfoHeader userInfoHeader, int page, int size, String sortBy, String order) {
        return deliveryService.getAllDeliveryDrivers(userInfoHeader, page, size, sortBy, order);
    }

    public Page<DeliveryInfo> searchDeliveryDrivers(UserInfoHeader userInfoHeader, String keyword, int page, int size, String sortBy, String order) {
        return deliveryService.searchDeliveryDrivers(userInfoHeader, keyword, page, size, sortBy, order);
    }
}
