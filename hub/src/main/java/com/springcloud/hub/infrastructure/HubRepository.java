package com.springcloud.hub.infrastructure;

import com.springcloud.hub.domain.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom {
}
