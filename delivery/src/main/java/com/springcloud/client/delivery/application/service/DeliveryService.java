package com.springcloud.client.delivery.application.service;


import com.springcloud.client.delivery.domain.delivery.Delivery;
import com.springcloud.client.delivery.infrastructure.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;


    public Page<Delivery> getDeliveries(Integer userId, String role, Pageable pageable) {

        return deliveryRepository.search(userId,role,pageable);

    }
}
