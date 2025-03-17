package com.springcloud.hub.interfaces;

import com.springcloud.hub.application.HubDto;
import com.springcloud.hub.application.HubRouteFacade;
import com.springcloud.hub.application.HubRouteResultDto;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.HubRoute;
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
    public ResponseEntity<ResponseDto<HubRoute>> getOptimalRoute(
            @RequestParam UUID startHubId,
            @RequestParam UUID goalHubId) {

        GetHubRouteRequestDto requestDto = new GetHubRouteRequestDto(startHubId, goalHubId);

        HubRoute routeInfo = hubRouteFacade.getOptimalRoute(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeInfo));
    }

    @PostMapping("/routes/naver/")
    public ResponseEntity<ResponseDto<List<HubRoute>>> createOptimalRoute(
            @RequestBody CreateHubRouteRquestDto requestDto) {

        List<HubRoute> routeList = hubRouteFacade.createBidirectionalRoutes(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeList));
    }

    @GetMapping("/routes/shortest-path")
    public ResponseEntity<ResponseDto<List<HubRouteResultDto>>> findShortestPath(
            @RequestParam UUID startHubId,
            @RequestParam UUID goalHubId) {

        GetHubRouteRequestDto requestDto = new GetHubRouteRequestDto(startHubId, goalHubId);

        List<HubRouteResultDto> shortestPath = hubRouteFacade.findShortestPath(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(shortestPath));
    }
}
