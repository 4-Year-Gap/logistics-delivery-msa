package com.springcloud.hub.interfaces.external;

import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.ListHubQuery;
import com.springcloud.hub.application.service.HubFacade;
import com.springcloud.hub.domain.service.UserRole;
import com.springcloud.hub.infrastructure.dto.KakaoMapApiResponse;
import com.springcloud.hub.interfaces.dto.*;
import com.springcloud.hub.interfaces.exception.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubFacade hubFacade;

    @PostMapping
    public ResponseEntity<ResponseDto<FindHubQuery>> createHub(
            @RequestBody CreateHubRequest requestDto,
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestHeader("X-USER-ROLE") UserRole role) {

        FindHubQuery findHubQuery = hubFacade.createHub(requestDto, userId, role);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(findHubQuery));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<ListHubQuery>> findHubs(
            @ModelAttribute SearchHubRequest requestDto, Pageable pageable) {

        ListHubQuery hubs = hubFacade.findHubs(requestDto, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(hubs));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto<FindHubQuery>> updateHub(
            @RequestBody UpdateHubRequest requestDto,
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestHeader("X-USER-ROLE") UserRole role) {

        FindHubQuery findHubQuery = hubFacade.updateHub(requestDto, userId, role);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(findHubQuery));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<FindHubQuery>> deleteHub(
            @RequestBody DeleteHubRequest requestDto,
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestHeader("X-USER-ROLE") UserRole role) {

        FindHubQuery findHubQuery = hubFacade.deleteHub(requestDto, userId, role);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(findHubQuery));
    }

    @GetMapping("/address/kakao")
    public ResponseEntity<ResponseDto<KakaoMapApiResponse>> getAddressLatitudeAndLongitude(
            @ModelAttribute FindAddressRequest requestDto, Pageable pageable) {

        KakaoMapApiResponse addressLatitudeAndLongitude = hubFacade.getAddressLatitudeAndLongitude(requestDto, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(addressLatitudeAndLongitude));
    }
}
