package com.springcloud.hub.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springcloud.hub.application.GetRouteCommand;
import com.springcloud.hub.application.HubRouteFacade;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.interfaces.exception.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hubs/naver")
@RequiredArgsConstructor
public class HubRouteController {

    private final HubRouteFacade hubRouteFacade;

    @GetMapping("/route")
    public ResponseEntity<ResponseDto<HubRoute>> getOptimalRoute(
            @RequestParam UUID startHubId,
            @RequestParam UUID goalHubId) {

        GetRouteCommand command = new GetRouteCommand(startHubId, goalHubId);

        HubRoute routeInfo = hubRouteFacade.getOptimalRoute(command);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeInfo));
    }

    @PostMapping("/route")
    public ResponseEntity<ResponseDto<List<HubRoute>>> createOptimalRoute(
            @RequestBody CreateHubRouteRquestDto requestDto) {

        GetRouteCommand command = new GetRouteCommand(requestDto.startHubId(), requestDto.goalHubId());

        List<HubRoute> routeList = hubRouteFacade.createBidirectionalRoutes(command);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeList));
    }
}
