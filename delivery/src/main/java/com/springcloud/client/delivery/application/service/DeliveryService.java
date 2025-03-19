package com.springcloud.client.delivery.application.service;


import com.springcloud.client.delivery.common.ApiResponse;
import com.springcloud.client.delivery.domain.delivery.Delivery;
import com.springcloud.client.delivery.infrastructure.dto.HubClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import com.springcloud.client.delivery.infrastructure.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;


    public Page<Delivery> getDeliveries(Integer userId, String role, Pageable pageable) {

        return deliveryRepository.search(userId,role,pageable);

    }

    public Delivery getDelivery(Integer userId, String role, UUID deliveryId) {


        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));



        return delivery;
    }

    @Transactional
    public void confirmDelivery(String orderId, HubClientResponse<List<HubRoute>> hubClientResponse) {

        


    }
}
