package com.springcloud.hub.application.dto;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.interfaces.dto.SearchHubRequest;

import java.math.BigDecimal;
import java.util.UUID;

public record FindHubQuery(UUID id, String name, BigDecimal latitude, BigDecimal longitude) {
    public static FindHubQuery fromFindHubQuery(Hub hub) {
        return new FindHubQuery(
                hub.getId(),
                hub.getName(),
                hub.getLatitude(),
                hub.getLongitude()
        );
    }

    public static FindHubQuery fromFindHubRequest(SearchHubRequest request) {
        return new FindHubQuery(
                null,
                request.address(),
                null,
                null
        );
    }
}