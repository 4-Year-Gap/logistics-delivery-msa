package com.springcloud.hub.application;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.domain.repository.HubRepository;
import com.springcloud.hub.domain.service.HubRouteDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NaverMapHubFacade {
    private final HubRepository hubRepository;
    private final HubRouteDomainService hubRouteDomainService;

    /**
     * 특정 허브에서 목적지까지 최적 경로 조회 (단일 허브)
     */
    public HubRoute getOptimalRoute(GetRouteCommand command) {
        Hub startHub = hubRepository.findHubById(command.startHubId())
                .orElseThrow(() -> new IllegalArgumentException("출발지 허브 정보를 찾을 수 없습니다: " + command.startHubId()));
        Hub goalHub = hubRepository.findHubById(command.goalHubId())
                .orElseThrow(() -> new IllegalArgumentException("도착지 허브 정보를 찾을 수 없습니다: " + command.goalHubId()));

        return hubRouteDomainService.fetchAndSaveRoute(startHub, goalHub);
    }

    /**
     * 모든 허브에서 목적지까지 경로 조회 (리스트)
     */
    public List<HubRoute> getOptimalRoutesForAllHubs() {
        List<Hub> hubs = hubRepository.findAllHubs();
        return hubRouteDomainService.getOptimalRoutesForAllHubs(hubs);
    }
}