package com.springcloud.hub.application.service;

import com.springcloud.hub.application.dto.*;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.repository.HubReader;
import com.springcloud.hub.domain.repository.HubRouteCacheStore;
import com.springcloud.hub.domain.service.HubRouteService;
import com.springcloud.hub.domain.service.HubService;
import com.springcloud.hub.interfaces.dto.FindHubRouteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HubRouteFacade {
    private final HubReader hubReader;
    private final HubRouteService hubRouteService;
    private final HubService hubService;
    private final HubRouteCacheStore hubRouteCacheStore;

    /**
     * 허브간 최소 거리 구하기 (다익스트라 알고리즘)
     */
    public List<GetHubRouteQuery> findShortestPath(FindHubRouteRequest requestDto) {
        // 캐시 검증
        List<GetHubRouteQuery> cachedRoute = hubRouteCacheStore.getShortestPath(requestDto, requestDto);

        if (cachedRoute != null) {
            return cachedRoute;
        }

        FindHubQuery fromHub = FindHubQuery.fromFindHubQuery(hubService.findHubById(requestDto.startHubId()));
        FindHubQuery toHub = FindHubQuery.fromFindHubQuery(hubService.findHubById(requestDto.goalHubId()));

        return hubRouteService.dijkstra(fromHub, toHub);
    }

    /**
     * 허브간 최소 거리 캐싱 웜업
     */
    public List<GetHubRouteQuery> cacheWarmUp() {
        List<Hub> hubs = hubReader.findAllHubs();

        ListHubQuery listHubQuery = ListHubQuery.fromEntities(hubs);

        //자기 자신을 제외하고 최소 거리 구하기
        List<Map<String, FindHubQuery>> routesExcludingSelfList = hubRouteService.getRoutesExcludingSelf(listHubQuery);

        for (Map<String, FindHubQuery> stringUUIDMap : routesExcludingSelfList) {
            FindHubQuery fromHub = stringUUIDMap.get("startHub");
            FindHubQuery toHub = stringUUIDMap.get("endHub");

            List<GetHubRouteQuery> dijkstra = hubRouteService.dijkstra(fromHub, toHub);

            hubRouteCacheStore.saveShortestPath(fromHub, toHub, dijkstra);
        }

        return null;
    }
}