package com.springcloud.hub.domain.service;

import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.FindNaverRouteQuery;
import com.springcloud.hub.application.dto.ListHubQuery;
import com.springcloud.hub.domain.repository.HubRouteReader;
import com.springcloud.hub.infrastructure.dto.GetHubRouteQuery;
import com.springcloud.hub.interfaces.exception.CustomNotFoundException;
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
     * 주어진 허브 라우터 ID를 기반으로 허브를 조회하고, 존재하지 않으면 예외를 던짐
     */
    public HubRoute findById(UUID hubId) {
        return hubRouteReader.findById(hubId)
                .orElseThrow(() -> new CustomNotFoundException("허브 라우터 정보를 찾을 수 없습니다."));
    }

    /**
     * 경로 정보를 기반으로 HubRoute 엔티티를 생성
     */
    public HubRoute createHubRoute(Hub startHub,
                                   Hub goalHub,
                                   FindNaverRouteQuery findNaverRouteQuery,
                                   UUID userId) {
        HubRoute hubRoute = HubRoute.builder()
                .Id(UUID.randomUUID())
                .fromHub(startHub)
                .toHub(goalHub)
                .timeRequired(findNaverRouteQuery.timeRequired())
                .moveDistance(findNaverRouteQuery.moveDistance())
                .build();

        hubRoute.create(String.valueOf(userId));

        return hubRoute;
    }

    /**
     * 양방향 HubRoute 생성
     */
    public List<HubRoute> createBidirectionalRoutes(Hub startHub,
                                                    Hub goalHub,
                                                    FindNaverRouteQuery forwardFindNaverRouteQuery,
                                                    FindNaverRouteQuery backwardFindNaverRouteQuery,
                                                    UUID userId) {
        HubRoute forwardRoute = createHubRoute(startHub, goalHub, forwardFindNaverRouteQuery, userId);
        HubRoute backwardRoute = createHubRoute(goalHub, startHub, backwardFindNaverRouteQuery, userId);
        return Arrays.asList(forwardRoute, backwardRoute);
    }

    /**
     * 다익스트라 알고리즘
     */
    public List<com.springcloud.hub.application.dto.GetHubRouteQuery> dijkstra(FindHubQuery start, FindHubQuery end) {
        Map<FindHubQuery, BigDecimal> distances = new HashMap<>();
        Map<FindHubQuery, FindHubQuery> previous = new HashMap<>();
        Map<FindHubQuery, GetHubRouteQuery> routeInfo = new HashMap<>();  // 경로 정보를 저장할 맵
        PriorityQueue<FindHubQuery> queue = new PriorityQueue<>(Comparator.comparing(distances::get));

        // 초기 거리 설정
        distances.put(start, BigDecimal.ZERO);
        queue.add(start);

        while (!queue.isEmpty()) {
            FindHubQuery current = queue.poll();

            // 목적지 도착 시 종료
            if (current.equals(end)) break;

            // 현재 허브에서 이동 가능한 경로 탐색
            List<GetHubRouteQuery> routes = hubRouteReader.findByFromHubWithToHub(current);

            for (GetHubRouteQuery route : routes) {
                FindHubQuery neighbor = FindHubQuery.fromFindHubQuery(route.getToHub());
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
        List<FindHubQuery> path = new ArrayList<>();
        for (FindHubQuery at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        // 결과 DTO 생성
        List<com.springcloud.hub.application.dto.GetHubRouteQuery> resultPath = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            FindHubQuery hub = path.get(i);

            if (i == 0) {
                // 첫 번째 허브는 이전 경로 정보가 없음
                resultPath.add(new com.springcloud.hub.application.dto.GetHubRouteQuery(hub, i + 1));
            } else {
                GetHubRouteQuery routeDetails = routeInfo.get(hub);
                resultPath.add(new com.springcloud.hub.application.dto.GetHubRouteQuery(
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

    /**
     * 자신을 제외한 모든 허브 접근 리스트
     */
    public List<Map<String, FindHubQuery>> getRoutesExcludingSelf(ListHubQuery hubs) {
        List<Map<String, FindHubQuery>> results = new ArrayList<>();

        for (FindHubQuery startHub : hubs.hubs()) {
            for (FindHubQuery goalHub : hubs.hubs()) {
                if (!startHub.id().equals(goalHub.id())) { // 자기 자신 제외
                    Map<String, FindHubQuery> routeMap = new HashMap<>();
                    routeMap.put("startHub", startHub);
                    routeMap.put("endHub", goalHub);
                    results.add(routeMap);
                }
            }
        }
        return results;
    }
}