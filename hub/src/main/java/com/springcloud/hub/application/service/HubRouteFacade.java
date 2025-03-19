package com.springcloud.hub.application.service;

import com.springcloud.hub.application.dto.*;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.domain.repository.HubReader;
import com.springcloud.hub.domain.repository.HubRouteCacheStore;
import com.springcloud.hub.domain.repository.HubRouteReader;
import com.springcloud.hub.domain.repository.HubRouteStore;
import com.springcloud.hub.infrastructure.external.NaverMapApiAdapter;
import com.springcloud.hub.domain.service.HubRouteService;
import com.springcloud.hub.interfaces.CreateHubRouteRequest;
import com.springcloud.hub.interfaces.FindHubRouteRequest;
import com.springcloud.hub.interfaces.exception.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteFacade {
    private final HubReader hubReader;
    private final HubRouteReader hubRouteReader;
    private final HubRouteStore hubRouteStore;
    private final HubRouteService hubRouteService;
    private final NaverMapApiAdapter naverMapApiAdapter;
    private final HubRouteCacheStore hubRouteCacheStore;

    /**
     * Naver Map API
     * 출발지와 목적지 허브 ID로 최적 경로를 조회
     */
    public FindHubRouteQuery getOptimalRoute(FindHubRouteRequest requestDto) {
        Hub startHub = findHubById(requestDto.startHubId());
        Hub goalHub = findHubById(requestDto.goalHubId());

        // 이미 저장된 경로가 있는지 확인
        Optional<HubRoute> existingRoute = hubRouteReader.findByFromHubAndToHub(startHub, goalHub);
        if (existingRoute.isPresent()) {
            throw new IllegalArgumentException("이미 저장된 경로가 있습니다.");
        }

        // 새 경로 생성
        FindNaverRouteQuery findNaverRouteQuery = naverMapApiAdapter.getOptimalRouteInfo(startHub, goalHub);
        HubRoute hubRoute = hubRouteService.createHubRoute(startHub, goalHub, findNaverRouteQuery);

        return new FindHubRouteQuery(hubRouteStore.save(hubRoute));
    }

    /**
     * Naver Map API
     * 출발지와 목적지 허브 ID로 양방향 최적 경로를 생성하고 저장
     */
    public HubRouteListCommand createBidirectionalRoutes(CreateHubRouteRequest requestDto) {
        Hub startHub = findHubById(requestDto.startHubId());
        Hub goalHub = findHubById(requestDto.goalHubId());

        // 양방향 경로 정보 조회
        FindNaverRouteQuery forwardFindNaverRouteQuery = naverMapApiAdapter.getOptimalRouteInfo(startHub, goalHub);
        FindNaverRouteQuery backwardFindNaverRouteQuery = naverMapApiAdapter.getOptimalRouteInfo(goalHub, startHub);

        // 양방향 경로 생성
        List<HubRoute> routes = hubRouteService.createBidirectionalRoutes(startHub, goalHub, forwardFindNaverRouteQuery, backwardFindNaverRouteQuery);

        // 저장 및 반환
        return hubRouteStore.saveAll(routes);
    }

    /**
     * 주어진 허브 ID를 기반으로 허브를 조회하고, 존재하지 않으면 예외를 던짐
     */
    private Hub findHubById(UUID hubId) {
        return hubReader.findHubById(hubId)
                .orElseThrow(() -> new CustomNotFoundException("허브 정보를 찾을 수 없습니다."));
    }

    /**
     * 허브간 최소 거리 구하기 (다익스트라 알고리즘)
     */
    public List<GetHubRouteQuery> findShortestPath(FindHubRouteRequest requestDto) {
        // 캐시 검증
        List<GetHubRouteQuery> cachedRoute = hubRouteCacheStore.getShortestPath(requestDto, requestDto);

        if (cachedRoute != null) {
            return cachedRoute;
        }

        FindHubQuery fromHub = new FindHubQuery(findHubById(requestDto.startHubId()));
        FindHubQuery toHub = new FindHubQuery(findHubById(requestDto.goalHubId()));

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