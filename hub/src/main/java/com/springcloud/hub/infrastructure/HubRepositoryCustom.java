package com.springcloud.hub.infrastructure;

import com.springcloud.hub.domain.entity.Hub;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepositoryCustom {
    Optional<Hub> findHubById(UUID hubId);  // 단건 조회
    List<Hub> findAllHubs();                // 전체 조회
}
