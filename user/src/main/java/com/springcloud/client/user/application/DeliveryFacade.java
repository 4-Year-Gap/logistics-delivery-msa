package com.springcloud.client.user.application;

import com.springcloud.client.user.domain.DeliveryCommand;
import com.springcloud.client.user.domain.DeliveryInfo;
import com.springcloud.client.user.domain.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryFacade {

    private final DeliveryService deliveryService;

    public DeliveryInfo addHubDeliveryDriver(DeliveryCommand command) {
        return deliveryService.addHubDeliveryDriver(command);
    }

    public DeliveryInfo getHubDeliveryDriver() {
        return deliveryService.getHubDeliveryDriver();
    }

    public DeliveryInfo addCompanyDeliveryDriver(DeliveryCommand command) {
        return deliveryService.addCompanyDeliveryDriver(command);
    }

    public DeliveryInfo getCompanyDeliveryDriver(UUID hubId) {
        return deliveryService.getCompanyDeliveryDriver(hubId);
    }

    public void deleteDeliveryDriver(DeliveryCommand command) {
        deliveryService.deleteDeliveryDriver(command);
    }
}
