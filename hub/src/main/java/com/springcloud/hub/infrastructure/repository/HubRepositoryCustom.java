package com.springcloud.hub.infrastructure.repository;

import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepositoryCustom {
    Optional<Hub> findHubById(UUID hubId);
    List<Hub> findAllHubs();
    Page<Hub> findAllHubs(FindHubQuery findHubQuery, Pageable pageable);
}
