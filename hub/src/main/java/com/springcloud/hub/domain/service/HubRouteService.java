package com.springcloud.hub.domain.service;

import com.springcloud.hub.application.HubDto;
import com.springcloud.hub.application.HubRouteResultDto;
import com.springcloud.hub.application.RouteInfo;
import com.springcloud.hub.domain.repository.HubRouteReader;
import com.springcloud.hub.infrastructure.dto.HubRouteDTO;
import org.springframework.stereotype.Service;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HubRouteService {
    private final HubRouteReader hubRouteReader;

    /**
     * 경로 정보를 기반으로 HubRoute 엔티티를 생성
     */
    public HubRoute createHubRoute(Hub startHub, Hub goalHub, RouteInfo routeInfo) {
        return HubRoute.builder()
                .Id(UUID.randomUUID())
                .fromHub(startHub)
                .toHub(goalHub)
                .timeRequired(routeInfo.timeRequired())
                .moveDistance(routeInfo.moveDistance())
                .isDeleted(false)
                .build();
    }

    /**
     * 양방향 HubRoute 생성
     */
    public List<HubRoute> createBidirectionalRoutes(Hub startHub, Hub goalHub, RouteInfo forwardRouteInfo, RouteInfo backwardRouteInfo) {
        HubRoute forwardRoute = createHubRoute(startHub, goalHub, forwardRouteInfo);
        HubRoute backwardRoute = createHubRoute(goalHub, startHub, backwardRouteInfo);
        return Arrays.asList(forwardRoute, backwardRoute);
    }

    /**
     * 다익스트라 알고리즘
     */
    public List<HubRouteResultDto> dijkstra(HubDto start, HubDto end) {
        Map<HubDto, BigDecimal> distances = new HashMap<>();
        Map<HubDto, HubDto> previous = new HashMap<>();
        Map<HubDto, HubRouteDTO> routeInfo = new HashMap<>();  // 경로 정보를 저장할 맵
        PriorityQueue<HubDto> queue = new PriorityQueue<>(Comparator.comparing(distances::get));

        // 초기 거리 설정
        distances.put(start, BigDecimal.ZERO);
        queue.add(start);

        while (!queue.isEmpty()) {
            HubDto current = queue.poll();

            // 목적지 도착 시 종료
            if (current.equals(end)) break;

            // 현재 허브에서 이동 가능한 경로 탐색
            List<HubRouteDTO> routes = hubRouteReader.findByFromHubWithToHub(current);

            for (HubRouteDTO route : routes) {
                HubDto neighbor = new HubDto(route.getToHub());
                //현재까지 이동한 거리 + 이번에 이동할 거리를 더해서 새로운 거리를 계산
                BigDecimal newDist = distances.get(current).add(route.getMoveDistance());

                // newDist가 기존에 저장된 거리보다 짧으면 업데이트
                // distances에 neighbor key가 없을 경우 Double의 가장 큰값으로 기본값 설정
                if (newDist.compareTo(distances.getOrDefault(neighbor, BigDecimal.valueOf(Double.MAX_VALUE))) < 0) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    routeInfo.put(neighbor, route);
                    queue.add(neighbor);
                }
            }
        }

        // 경로 역추적해서 list에 저장
        List<HubDto> path = new ArrayList<>();
        for (HubDto at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        // 결과 DTO 생성
        List<HubRouteResultDto> resultPath = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            HubDto hub = path.get(i);

            if (i == 0) {
                // 첫 번째 허브는 이전 경로 정보가 없음
                resultPath.add(new HubRouteResultDto(hub, i + 1));
            } else {
                HubRouteDTO routeDetails = routeInfo.get(hub);
                resultPath.add(new HubRouteResultDto(
                        hub,
                        i + 1,  // 시퀀스 번호
                        routeDetails.getMoveDistance(),
                        routeDetails.getTimeRequired(),
                        distances.get(hub)  // 시작점에서 현재 허브까지의 누적 거리
                ));
            }
        }

        return resultPath;
    }
}