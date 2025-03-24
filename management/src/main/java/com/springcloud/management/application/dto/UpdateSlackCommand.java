package com.springcloud.management.application.dto;

import com.springcloud.management.domain.entity.Contents;
import com.springcloud.management.domain.entity.Slack;
import com.springcloud.management.interfaces.dto.UpdateSlackRequest;

import java.util.UUID;

public record UpdateSlackCommand(UUID id,
                                 String content,
                                 UUID userId) {
    public static UpdateSlackCommand fromUpdateHubRequest(UpdateSlackRequest updateSlackRequest, UUID userId){
        return new UpdateSlackCommand(
                updateSlackRequest.slackId(),
                updateSlackRequest.contents(),
                userId
        );
    }

    public Slack toEntity(Slack slack) {
        Slack slackBuild = Slack.builder()
                .id(slack.getId())
                .contents(Contents.create(content))
                .userId(userId)
                .build();

        slackBuild.update(String.valueOf(userId));

        return slackBuild;
    }
}
