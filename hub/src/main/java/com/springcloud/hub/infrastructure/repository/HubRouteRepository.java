package com.springcloud.hub.infrastructure.repository;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID> {
    Optional<HubRoute> findByFromHubAndToHub(Hub startHub, Hub goalHub);
}
