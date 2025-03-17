package com.springcloud.hub.infrastructure.repository;

import com.springcloud.hub.domain.entity.Hub;

import java.util.Optional;
import java.util.UUID;

public interface HubRepositoryCustom {
    Optional<Hub> findHubById(UUID hubId);  // 단건 조회
}
