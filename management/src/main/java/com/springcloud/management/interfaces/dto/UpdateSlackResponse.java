package com.springcloud.management.interfaces.dto;


import com.springcloud.management.domain.entity.Contents;
import com.springcloud.management.domain.entity.Slack;

import java.util.UUID;

public record UpdateSlackResponse(UUID slackId,
                                  Contents contents,
                                  UUID userId) {

    public static UpdateSlackResponse fromEntity(Slack slack){
        return new UpdateSlackResponse(
                slack.getId(),
                slack.getContents(),
                slack.getUserId()
        );
    }
}
