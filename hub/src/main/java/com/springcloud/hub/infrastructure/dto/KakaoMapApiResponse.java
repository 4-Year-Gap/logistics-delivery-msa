package com.springcloud.hub.infrastructure.dto;

import java.util.List;

public record KakaoMapApiResponse(List<Document> documents) {
    public record Document(
            String address_name,
            String x,
            String y
    ) {}
}