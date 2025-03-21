package com.springcloud.hub.domain.repository;

import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.GetHubRouteCacheQuery;
import com.springcloud.hub.application.dto.GetHubRouteQuery;
import com.springcloud.hub.interfaces.dto.GetHubRouteRequest;

import java.util.List;

public interface HubRouteCacheStore {
    void saveShortestPath(FindHubQuery fromHub, FindHubQuery toHub, List<GetHubRouteQuery> route);
    List<GetHubRouteQuery> getShortestPath(GetHubRouteCacheQuery getHubRouteCacheQuery);
}