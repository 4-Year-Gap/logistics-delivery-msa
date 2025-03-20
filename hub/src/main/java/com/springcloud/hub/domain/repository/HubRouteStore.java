package com.springcloud.hub.domain.repository;

import com.springcloud.hub.application.dto.HubRouteListCommand;
import com.springcloud.hub.domain.entity.HubRoute;

import java.util.List;

public interface HubRouteStore {
    HubRoute save(HubRoute route);
    HubRouteListCommand saveAll(List<HubRoute> routes);
}
