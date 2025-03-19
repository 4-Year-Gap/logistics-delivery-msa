package com.springcloud.hub.domain.repository;

import com.springcloud.hub.domain.entity.Hub;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubReader {
    Optional<Hub> findHubById(UUID hubId);  // 단건 조회
    List<Hub> findAllHubs();  // 리스트 조회
}
