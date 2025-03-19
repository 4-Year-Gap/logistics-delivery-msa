package com.springcloud.hub.application.service;

import com.springcloud.hub.application.dto.FindAddressQuery;
import com.springcloud.hub.application.dto.FindHubRouteQuery;
import com.springcloud.hub.application.dto.FindNaverRouteQuery;
import com.springcloud.hub.application.dto.HubRouteListCommand;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.domain.repository.HubRouteReader;
import com.springcloud.hub.domain.repository.HubRouteStore;
import com.springcloud.hub.domain.service.HubRouteService;
import com.springcloud.hub.domain.service.HubService;
import com.springcloud.hub.infrastructure.dto.KakaoMapApiResponse;
import com.springcloud.hub.infrastructure.external.MapFinder;
import com.springcloud.hub.interfaces.dto.CreateHubRouteRequest;
import com.springcloud.hub.interfaces.dto.FindAddressRequest;
import com.springcloud.hub.interfaces.dto.FindHubRouteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapApiFacade {

    private final HubRouteReader hubRouteReader;
    private final HubRouteStore hubRouteStore;
    private final MapFinder mapFinder;
    private final HubService hubService;
    private final HubRouteService hubRouteService;

    /**
     * Kakao Map API에서 주소지 검색한 주소지와 위도 경도를 반환
     */
    public KakaoMapApiResponse getAddressLatitudeAndLongitude(FindAddressRequest request, Pageable pageable) {
        FindAddressQuery findAddressQuery = FindAddressQuery.fromRequestParam(request);

        return mapFinder.getLatitudeAndLongitude(findAddressQuery, pageable);
    }

    /**
     * Naver Map API
     * 출발지와 목적지 허브 ID로 최적 경로를 조회
     */
    public FindHubRouteQuery getOptimalRoute(FindHubRouteRequest requestDto) {
        Hub startHub = hubService.findHubById(requestDto.startHubId());
        Hub goalHub = hubService.findHubById(requestDto.goalHubId());

        // 이미 저장된 경로가 있는지 확인
        Optional<HubRoute> existingRoute = hubRouteReader.findByFromHubAndToHub(startHub, goalHub);
        if (existingRoute.isPresent()) {
            throw new IllegalArgumentException("이미 저장된 경로가 있습니다.");
        }

        // 새 경로 생성
        FindNaverRouteQuery findNaverRouteQuery = mapFinder.getOptimalRouteInfo(startHub, goalHub);
        HubRoute hubRoute = hubRouteService.createHubRoute(startHub, goalHub, findNaverRouteQuery);

        return new FindHubRouteQuery(hubRouteStore.save(hubRoute));
    }

    /**
     * Naver Map API
     * 출발지와 목적지 허브 ID로 양방향 최적 경로를 생성하고 저장
     */
    public HubRouteListCommand createBidirectionalRoutes(CreateHubRouteRequest requestDto) {
        Hub startHub = hubService.findHubById(requestDto.startHubId());
        Hub goalHub = hubService.findHubById(requestDto.goalHubId());

        // 양방향 경로 정보 조회
        FindNaverRouteQuery forwardFindNaverRouteQuery = mapFinder.getOptimalRouteInfo(startHub, goalHub);
        FindNaverRouteQuery backwardFindNaverRouteQuery = mapFinder.getOptimalRouteInfo(goalHub, startHub);

        // 양방향 경로 생성
        List<HubRoute> routes = hubRouteService.createBidirectionalRoutes(startHub, goalHub, forwardFindNaverRouteQuery, backwardFindNaverRouteQuery);

        // 저장 및 반환
        return hubRouteStore.saveAll(routes);
    }
}
