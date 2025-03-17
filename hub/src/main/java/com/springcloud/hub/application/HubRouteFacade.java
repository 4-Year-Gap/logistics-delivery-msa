package com.springcloud.hub.application;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.infrastructure.external.HubRouteClient;
import com.springcloud.hub.infrastructure.external.NaverMapApiAdapter;
import com.springcloud.hub.infrastructure.repository.HubRepository;
import com.springcloud.hub.domain.service.HubRouteService;
import com.springcloud.hub.infrastructure.repository.HubRouteRepository;
import com.springcloud.hub.interfaces.CreateHubRouteRquestDto;
import com.springcloud.hub.interfaces.GetHubRouteRequestDto;
import com.springcloud.hub.interfaces.exception.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteFacade {
    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final HubRouteService hubRouteService;
    private final NaverMapApiAdapter naverMapApiAdapter;
    private final HubRouteClient hubRouteClient;

    /**
     * Naver Map API
     * 출발지와 목적지 허브 ID로 최적 경로를 조회
     */
    public HubRoute getOptimalRoute(GetHubRouteRequestDto requestDto) {
        Hub startHub = findHubById(requestDto.startHubId());
        Hub goalHub = findHubById(requestDto.goalHubId());

        // 이미 저장된 경로가 있는지 확인
        Optional<HubRoute> existingRoute = hubRouteRepository.findByFromHubAndToHub(startHub, goalHub);
        if (existingRoute.isPresent()) {
            throw new IllegalArgumentException("이미 저장된 경로가 있습니다.");
        }

        // 새 경로 생성
        RouteInfo routeInfo = naverMapApiAdapter.getOptimalRouteInfo(startHub, goalHub);
        HubRoute hubRoute = hubRouteService.createHubRoute(startHub, goalHub, routeInfo);

        return hubRouteRepository.save(hubRoute);
    }

    /**
     * Naver Map API
     * 출발지와 목적지 허브 ID로 양방향 최적 경로를 생성하고 저장
     */
    public List<HubRoute> createBidirectionalRoutes(CreateHubRouteRquestDto requestDto) {
        Hub startHub = findHubById(requestDto.startHubId());
        Hub goalHub = findHubById(requestDto.goalHubId());

        // 양방향 경로 정보 조회
        RouteInfo forwardRouteInfo = naverMapApiAdapter.getOptimalRouteInfo(startHub, goalHub);
        RouteInfo backwardRouteInfo = naverMapApiAdapter.getOptimalRouteInfo(goalHub, startHub);

        // 양방향 경로 생성
        List<HubRoute> routes = hubRouteService.createBidirectionalRoutes(startHub, goalHub, forwardRouteInfo, backwardRouteInfo);

        // 저장 및 반환
        return hubRouteRepository.saveAll(routes);
    }

    /**
     * 주어진 허브 ID를 기반으로 허브를 조회하고, 존재하지 않으면 예외를 던짐
     */
    private Hub findHubById(UUID hubId) {
        return hubRepository.findHubById(hubId)
                .orElseThrow(() -> new CustomNotFoundException("허브 정보를 찾을 수 없습니다."));
    }

    /**
     * 허브간 최소 거리 구하기 (다익스트라 알고리즘)
     */
    public List<HubRouteResultDto> findShortestPath(GetHubRouteRequestDto requestDto) {
        HubDto fromHub = new HubDto(findHubById(requestDto.startHubId()));
        HubDto toHub = new HubDto(findHubById(requestDto.goalHubId()));

        return hubRouteService.dijkstra(fromHub, toHub);
    }
}