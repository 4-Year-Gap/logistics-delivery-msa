package com.springcloud.client.delivery.infrastructure.repository;

import com.springcloud.client.delivery.domain.delivery.Delivery;
import com.springcloud.client.delivery.domain.delivery.DeliveryHubRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryHubRouteRepository extends JpaRepository<DeliveryHubRoute, UUID> ,CustomDeliveryHubRouteRepository {

}
