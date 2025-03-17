package com.springcloud.client.delivery.infrastructure.repository;

import com.springcloud.client.delivery.domain.delivery.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomDeliveryRepository {

    Page<Delivery> search(Integer userId, String role, Pageable pageable);
}
