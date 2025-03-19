package com.springcloud.hub.application.dto;



import com.springcloud.hub.domain.entity.Hub;

import java.util.List;

public record ListHubQuery(List<FindHubQuery> hubs) {
    public static ListHubQuery fromEntities(List<Hub> entityHub) {
        return new ListHubQuery(entityHub.stream().map(FindHubQuery::new).toList());
    }
}