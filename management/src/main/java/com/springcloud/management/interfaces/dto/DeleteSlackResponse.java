package com.springcloud.management.interfaces.dto;


import com.springcloud.management.domain.entity.Contents;
import com.springcloud.management.domain.entity.Slack;

import java.util.UUID;

public record DeleteSlackResponse(UUID slackId,
                                  Contents contents,
                                  UUID userId) {

    public static DeleteSlackResponse fromEntity(Slack slack){
        return new DeleteSlackResponse(
                slack.getId(),
                slack.getContents(),
                slack.getUserId()
        );
    }
}
