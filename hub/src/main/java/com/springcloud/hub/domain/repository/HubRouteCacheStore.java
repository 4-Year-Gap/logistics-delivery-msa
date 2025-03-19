package com.springcloud.hub.domain.repository;

import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.GetHubRouteQuery;
import com.springcloud.hub.interfaces.FindHubRouteRequest;

import java.util.List;

public interface HubRouteCacheStore {
    void saveShortestPath(FindHubQuery fromHub, FindHubQuery toHub, List<GetHubRouteQuery> route);
    List<GetHubRouteQuery> getShortestPath(FindHubRouteRequest fromHub, FindHubRouteRequest toHub);
}