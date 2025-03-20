package com.springcloud.hub.application.service;

import com.springcloud.hub.application.dto.*;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.domain.repository.HubReader;
import com.springcloud.hub.domain.repository.HubRouteCacheStore;
import com.springcloud.hub.domain.repository.HubRouteReader;
import com.springcloud.hub.domain.repository.HubRouteStore;
import com.springcloud.hub.domain.service.HubRouteService;
import com.springcloud.hub.domain.service.HubService;
import com.springcloud.hub.infrastructure.dto.ListHubRouteQuery;
import com.springcloud.hub.infrastructure.external.MapFinder;
import com.springcloud.hub.interfaces.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final HubRouteReader hubRouteReader;
    private final HubRouteStore hubRouteStore;
    private final MapFinder mapFinder;

    /**
     * 출발지와 목적지 허브 ID로 최적 경로를 조회(Naver Map API)
     */
    public FindNaverRouteQuery getOptimalRoute(GetHubRouteRequest requestDto) {
        Hub startHub = hubService.findById(requestDto.startHubId());
        Hub goalHub = hubService.findById(requestDto.goalHubId());

        return mapFinder.getOptimalRouteInfo(startHub, goalHub);
    }

    /**
     * 출발지와 목적지 허브 ID로 양방향 최적 경로를 생성하고 저장(Naver Map API)
     */
    public HubRouteListCommand createBidirectionalRoutes(CreateHubRouteRequest requestDto) {
        Hub startHub = hubService.findById(requestDto.startHubId());
        Hub goalHub = hubService.findById(requestDto.goalHubId());

        // 양방향 경로 정보 조회
        FindNaverRouteQuery forwardFindNaverRouteQuery = mapFinder.getOptimalRouteInfo(startHub, goalHub);
        FindNaverRouteQuery backwardFindNaverRouteQuery = mapFinder.getOptimalRouteInfo(goalHub, startHub);

        // 양방향 경로 생성
        List<HubRoute> routes = hubRouteService.createBidirectionalRoutes(startHub, goalHub, forwardFindNaverRouteQuery, backwardFindNaverRouteQuery);

        // 저장 및 반환
        return hubRouteStore.saveAll(routes);
    }

    /**
     * 허브간 최소 거리 구하기 (다익스트라 알고리즘)
     */
    public List<GetHubRouteQuery> findShortestPath(GetHubRouteRequest requestDto) {
        // 캐시 검증
        List<GetHubRouteQuery> cachedRoute = hubRouteCacheStore.getShortestPath(requestDto, requestDto);

        if (cachedRoute != null) {
            return cachedRoute;
        }

        FindHubQuery fromHub = FindHubQuery.fromFindHubQuery(hubService.findById(requestDto.startHubId()));
        FindHubQuery toHub = FindHubQuery.fromFindHubQuery(hubService.findById(requestDto.goalHubId()));

        return hubRouteService.dijkstra(fromHub, toHub);
    }

    /**
     * 허브간 최소 거리 캐싱 웜업
     */
    public void cacheWarmUp() {
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
    }

    public Page<ListHubRouteQuery> findHubRoutes(SearchHubRouteRequest requestDto, Pageable pageable) {
        SearchHubRouteQuery searchHubRouteQuery = SearchHubRouteQuery.fromSearchHubRouteRequest(requestDto);
        return hubRouteReader.findByAddressAndLatitudeAndLongitude(searchHubRouteQuery, pageable);
    }

    public FindHubRouteQuery updateHubRoute(UpdateHubRouteRequest requestDto) {
        UpdateHubRouteCommand command = UpdateHubRouteCommand.fromUpdateHubRouteRequest(requestDto);
        HubRoute updatedHub = command.toEntity(hubRouteService.findById(command.id()));
        return FindHubRouteQuery.fromHubRouteEntity(hubRouteStore.save(updatedHub));
    }

    public FindHubRouteQuery deleteHubRoute(DeleteHubRouteRequest requestDto) {
        DeleteHubRouteCommand command = DeleteHubRouteCommand.fromDeleteHubRequest(requestDto);
        HubRoute updatedHub = command.toEntity(hubRouteService.findById(command.id()));
        return FindHubRouteQuery.fromHubRouteEntity(hubRouteStore.save(updatedHub));
    }
}