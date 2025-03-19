package com.springcloud.hub.interfaces;

import com.springcloud.hub.application.dto.FindHubRouteQuery;
import com.springcloud.hub.application.dto.HubRouteListCommand;
import com.springcloud.hub.application.service.MapApiFacade;
import com.springcloud.hub.infrastructure.dto.KakaoMapApiResponse;
import com.springcloud.hub.interfaces.dto.CreateHubRouteRequest;
import com.springcloud.hub.interfaces.dto.FindAddressRequest;
import com.springcloud.hub.interfaces.dto.FindHubRouteRequest;
import com.springcloud.hub.interfaces.exception.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapApiController {

    private final MapApiFacade mapApiFacade;

    @GetMapping("/address/kakao")
    public ResponseEntity<ResponseDto<KakaoMapApiResponse>> getAddressLatitudeAndLongitude(
            @ModelAttribute FindAddressRequest requestDto, Pageable pageable) {

        KakaoMapApiResponse addressLatitudeAndLongitude = mapApiFacade.getAddressLatitudeAndLongitude(requestDto, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(addressLatitudeAndLongitude));
    }

    @GetMapping("/routes/naver")
    public ResponseEntity<ResponseDto<FindHubRouteQuery>> getOptimalRoute(
            @RequestParam UUID startHubId,
            @RequestParam UUID goalHubId) {

        FindHubRouteRequest requestDto = new FindHubRouteRequest(startHubId, goalHubId);

        FindHubRouteQuery routeInfo = mapApiFacade.getOptimalRoute(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeInfo));
    }

    @PostMapping("/routes/naver")
    public ResponseEntity<ResponseDto<HubRouteListCommand>> createOptimalRoute(
            @RequestBody CreateHubRouteRequest requestDto) {

        HubRouteListCommand routeList = mapApiFacade.createBidirectionalRoutes(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(routeList));
    }
}
