package com.springcloud.hub.application;

import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.domain.service.NaverMapHubApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hubs")
@RequiredArgsConstructor
public class NaverHubApiController {

    private final NaverMapHubApiService naverMapHubApiService;

    @GetMapping("/{startHubId}/route/{goalHubId}")
    public ResponseEntity<HubRoute> getRoute(
            @PathVariable UUID startHubId,
            @PathVariable UUID goalHubId) {

        HubRoute routeInfo = naverMapHubApiService.getOptimalRoute(startHubId, goalHubId);
        return ResponseEntity.ok(routeInfo);
    }

    @GetMapping("/route/list")
    public ResponseEntity<List<HubRoute>> getRouteList() {
        List<HubRoute> optimalRoutesForAllHubs = naverMapHubApiService.getOptimalRoutesForAllHubs();
        return ResponseEntity.ok(optimalRoutesForAllHubs);
    }
}
