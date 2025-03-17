package com.springcloud.hub.infrastructure.repository;

import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.domain.repository.HubRouteReader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID>, HubRouteReader, HubRouteRepositoryCustom {
}
