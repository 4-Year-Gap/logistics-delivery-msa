package com.springcloud.hub.infrastructure.external;

import com.springcloud.hub.application.dto.CreateHubCommand;
import com.springcloud.hub.application.dto.FindAddressQuery;
import com.springcloud.hub.application.dto.FindNaverRouteQuery;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.infrastructure.dto.KakaoMapApiResponse;
import org.springframework.data.domain.Pageable;

public interface MapFinder {
    FindNaverRouteQuery getOptimalRouteInfo(Hub startHub, Hub goalHub);
    KakaoMapApiResponse getLatitudeAndLongitude(FindAddressQuery query, Pageable pageable);
}
