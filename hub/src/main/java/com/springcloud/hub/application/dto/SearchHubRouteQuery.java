package com.springcloud.hub.application.dto;


import com.springcloud.hub.interfaces.dto.SearchHubRouteRequest;

import java.math.BigDecimal;


public record SearchHubRouteQuery(
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static SearchHubRouteQuery fromSearchHubRouteRequest(SearchHubRouteRequest requestDto) {
        return new SearchHubRouteQuery(
                requestDto.address(),
                requestDto.latitude(),
                requestDto.longitude()
        );
    }
}

