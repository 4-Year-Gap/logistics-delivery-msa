package com.springcloud.management.interfaces.dto;


import com.springcloud.management.domain.entity.Contents;
import com.springcloud.management.domain.entity.Slack;

import java.util.UUID;

public record SearchSlackResponse(UUID slackId,
                                  Contents contents,
                                  UUID userId) {

    public static SearchSlackResponse fromEntity(Slack slack){
        return new SearchSlackResponse(
                slack.getId(),
                slack.getContents(),
                slack.getUserId()
        );
    }
}
