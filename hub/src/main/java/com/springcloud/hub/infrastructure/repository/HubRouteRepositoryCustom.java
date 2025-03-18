package com.springcloud.hub.infrastructure.repository;

import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.GetHubRouteQuery;
import com.springcloud.hub.application.dto.HubRouteListCommand;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.infrastructure.dto.FindHubRouteQuery;

import java.util.List;

public interface HubRouteRepositoryCustom {
    List<FindHubRouteQuery> findByFromHubWithToHub(FindHubQuery hub);
    HubRouteListCommand saveAll(List<HubRoute> routes);
}
