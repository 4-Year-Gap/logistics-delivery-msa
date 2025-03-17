package com.springcloud.hub.infrastructure.repository;

import com.springcloud.hub.application.HubDto;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.infrastructure.dto.HubRouteDTO;

import java.util.List;

public interface HubRouteRepositoryCustom {
    List<HubRouteDTO> findByFromHubWithToHub(HubDto hub);
}
