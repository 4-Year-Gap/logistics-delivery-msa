package com.springcloud.client.user.application;

import com.springcloud.client.user.domain.DeliveryInfo;
import com.springcloud.client.user.domain.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryFacade {

    private final DeliveryService deliveryService;

    public DeliveryInfo getHubDeliveryDriver() {
        return deliveryService.getHubDeliveryDriver();
    }
}
