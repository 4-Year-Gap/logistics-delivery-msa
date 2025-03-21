package com.springcloud.hub.application.dto;

import com.springcloud.hub.interfaces.dto.SearchHubRequest;

public record SearchHubQuery(String name) {
    public static SearchHubQuery fromSearchHubRequest(SearchHubRequest request) {
        return new SearchHubQuery(
                request.address()
        );
    }
}