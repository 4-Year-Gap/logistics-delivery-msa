package com.springcloud.hub.application.dto;



import com.springcloud.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record ListHubQuery(List<FindHubQuery> hubs) {
    public static ListHubQuery fromEntities(List<Hub> entityHub) {
        return new ListHubQuery(entityHub.stream().map(FindHubQuery::fromFindHubQuery).toList());
    }

    public static ListHubQuery fromEntities(Page<Hub> entityHub) {
        List<FindHubQuery> hubQueries = entityHub.stream()
                .map(hub -> new FindHubQuery(
                        hub.getId(),
                        hub.getName(),
                        hub.getLatitude(),
                        hub.getLongitude()
                ))
                .collect(Collectors.toList());

        return new ListHubQuery(hubQueries);
    }
}