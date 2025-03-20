package com.springcloud.hub.interfaces;

import com.springcloud.hub.application.dto.*;
import com.springcloud.hub.application.service.HubRouteFacade;
import com.springcloud.hub.infrastructure.dto.ListHubRouteQuery;
import com.springcloud.hub.interfaces.dto.*;
import com.springcloud.hub.interfaces.exception.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hub/routes")
@RequiredArgsConstructor
public class HubRouteController {

    private final HubRouteFacade hubRouteFacade;

    @GetMapping("/shortest-path")
    public ResponseEntity<ResponseDto<List<GetHubRouteQuery>>> findShortestPath(
            @RequestParam UUID startHubId,
            @RequestParam UUID goalHubId) {

        GetHubRouteRequest requestDto = new GetHubRouteRequest(startHubId, goalHubId);
        List<GetHubRouteQuery> shortestPath = hubRouteFacade.findShortestPath(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(shortestPath));
    }

    @GetMapping("/cache/warmup")
    public ResponseEntity<ResponseDto<String>> cacheWarmUp() {
        hubRouteFacade.cacheWarmUp();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success("complete"));
    }

    @GetMapping("/naver")
    public ResponseEntity<ResponseDto<FindNaverRouteQuery>> getOptimalRoute(
            @RequestParam UUID startHubId,
            @RequestParam UUID goalHubId) {

        GetHubRouteRequest requestDto = new GetHubRouteRequest(startHubId, goalHubId);
        FindNaverRouteQuery routeInfo = hubRouteFacade.getOptimalRoute(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeInfo));
    }

    @PostMapping("/naver")
    public ResponseEntity<ResponseDto<HubRouteListCommand>> createOptimalRoute(
            @RequestBody CreateHubRouteRequest requestDto) {
        HubRouteListCommand routeList = hubRouteFacade.createBidirectionalRoutes(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeList));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<ListHubRouteQuery>>> findHubRoutes(
            @ModelAttribute SearchHubRouteRequest requestDto, Pageable pageable) {
        Page<ListHubRouteQuery> listHubRouteQueries = hubRouteFacade.findHubRoutes(requestDto, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(listHubRouteQueries));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto<FindHubRouteQuery>> updateHubRoute(
            @RequestBody UpdateHubRouteRequest requestDto) {

        FindHubRouteQuery findHubQuery = hubRouteFacade.updateHubRoute(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(findHubQuery));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<FindHubRouteQuery>> deleteHubRoute(
            @RequestBody DeleteHubRouteRequest requestDto) {

        FindHubRouteQuery findHubQuery = hubRouteFacade.deleteHubRoute(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(findHubQuery));
    }
}
