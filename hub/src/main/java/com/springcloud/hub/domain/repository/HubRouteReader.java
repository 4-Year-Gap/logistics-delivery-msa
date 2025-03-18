package com.springcloud.hub.domain.repository;

import com.springcloud.hub.application.HubDto;
import com.springcloud.hub.application.HubRouteCommand;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.infrastructure.dto.HubRouteDTO;

import java.util.List;
import java.util.Optional;

public interface HubRouteReader {
    Optional<HubRoute> findByFromHubAndToHub(Hub startHub, Hub goalHub);
    List<HubRouteDTO> findByFromHubWithToHub(HubDto hub);
}
