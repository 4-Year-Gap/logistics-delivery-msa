package com.springcloud.hub.application.dto;

import com.springcloud.hub.interfaces.dto.SearchHubRequest;

import java.util.UUID;

public record SearchHubQuery(String name,
                             UUID hubId) {
    public static SearchHubQuery fromSearchHubRequest(SearchHubRequest request) {
        return new SearchHubQuery(
                request.address(),
                request.hubId()
        );
    }
}