package com.springcloud.management.application.dto;


import com.springcloud.management.domain.entity.Slack;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record ListSlackQuery(List<FindSlackQuery> hubs) {
    public static ListSlackQuery fromEntities(List<Slack> entityHub) {
        return new ListSlackQuery(entityHub.stream().map(FindSlackQuery::fromFindSlackQuery).toList());
    }

    public static ListSlackQuery fromEntities(Page<Slack> entitySlack) {
        List<FindSlackQuery> slackQueries = entitySlack.stream()
                .map(slack -> new FindSlackQuery(
                        slack.getId(),
                        slack.getContents()
                ))
                .collect(Collectors.toList());

        return new ListSlackQuery(slackQueries);
    }
}