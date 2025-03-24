package com.springcloud.management.application.dto;


import com.springcloud.management.interfaces.dto.SearchSlackRequest;

public record SearchSlackQuery(String message) {
    public static SearchSlackQuery fromSearchHubRequest(SearchSlackRequest request) {
        return new SearchSlackQuery(
                request.message()
        );
    }
}