package com.springcloud.management.application.dto;

import com.springcloud.management.domain.entity.Contents;
import com.springcloud.management.domain.entity.Slack;

import java.math.BigDecimal;
import java.util.UUID;

public record FindSlackQuery(UUID slackId, Contents contents) {
    public static FindSlackQuery fromFindSlackQuery(Slack slack) {
        return new FindSlackQuery(
                slack.getId(),
                slack.getContents()
        );
    }
}