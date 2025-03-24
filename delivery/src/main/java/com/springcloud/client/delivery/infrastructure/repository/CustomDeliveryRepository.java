package com.springcloud.client.delivery.infrastructure.repository;

import com.springcloud.client.delivery.domain.delivery.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomDeliveryRepository {

    Page<Delivery> search(UUID userId, String role, Pageable pageable,UUID hubId);

}
