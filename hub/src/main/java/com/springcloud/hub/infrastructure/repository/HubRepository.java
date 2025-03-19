package com.springcloud.hub.infrastructure.repository;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.repository.HubReader;
import com.springcloud.hub.domain.repository.HubStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom, HubReader, HubStore {
}
