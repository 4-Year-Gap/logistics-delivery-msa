package com.springcloud.management.interfaces.dto;


import com.springcloud.management.domain.entity.Contents;
import com.springcloud.management.domain.entity.Slack;

import java.util.UUID;

public record CreateSlackResponse(UUID slackId,
                                  Contents contents,
                                  UUID userId) {

    public static CreateSlackResponse fromEntity(Slack slack){
        return new CreateSlackResponse(
                slack.getId(),
                slack.getContents(),
                slack.getUserId()
        );
    }
}
