package com.springcloud.hub.infrastructure;

import com.springcloud.hub.domain.entity.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID> {
}
