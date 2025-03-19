package com.springcloud.hub.interfaces;

import com.springcloud.hub.application.dto.FindHubRouteQuery;
import com.springcloud.hub.application.dto.GetHubRouteQuery;
import com.springcloud.hub.application.dto.HubRouteListCommand;
import com.springcloud.hub.application.service.HubRouteFacade;
import com.springcloud.hub.interfaces.exception.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hub")
@RequiredArgsConstructor
public class HubRouteController {

    private final HubRouteFacade hubRouteFacade;

    @GetMapping("/routes/naver")
    public ResponseEntity<ResponseDto<FindHubRouteQuery>> getOptimalRoute(
            @RequestParam UUID startHubId,
            @RequestParam UUID goalHubId) {

        FindHubRouteRequest requestDto = new FindHubRouteRequest(startHubId, goalHubId);

        FindHubRouteQuery routeInfo = hubRouteFacade.getOptimalRoute(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeInfo));
    }

    @PostMapping("/routes/naver")
    public ResponseEntity<ResponseDto<HubRouteListCommand>> createOptimalRoute(
            @RequestBody CreateHubRouteRequest requestDto) {

        HubRouteListCommand routeList = hubRouteFacade.createBidirectionalRoutes(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeList));
    }

    @GetMapping("/routes/shortest-path")
    public ResponseEntity<ResponseDto<List<GetHubRouteQuery>>> findShortestPath(
            @RequestParam UUID startHubId,
            @RequestParam UUID goalHubId) {

        FindHubRouteRequest requestDto = new FindHubRouteRequest(startHubId, goalHubId);

        List<GetHubRouteQuery> shortestPath = hubRouteFacade.findShortestPath(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(shortestPath));
    }

    @GetMapping("/routes/cache/warmup")
    public ResponseEntity<ResponseDto<List<GetHubRouteQuery>>> cacheWarmUp() {

        List<GetHubRouteQuery> shortestPath = hubRouteFacade.cacheWarmUp();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(shortestPath));
    }
}
