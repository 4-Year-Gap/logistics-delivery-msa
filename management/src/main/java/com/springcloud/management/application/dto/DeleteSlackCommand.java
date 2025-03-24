package com.springcloud.management.application.dto;

import com.springcloud.management.domain.entity.Contents;
import com.springcloud.management.domain.entity.Slack;
import com.springcloud.management.interfaces.dto.DeleteSlackRequest;
import com.springcloud.management.interfaces.dto.UpdateSlackRequest;

import java.util.UUID;

public record DeleteSlackCommand(UUID id,
                                 UUID userId) {
    public static DeleteSlackCommand fromDeleteHubRequest(DeleteSlackRequest deleteSlackRequest, UUID userId){
        return new DeleteSlackCommand(
                deleteSlackRequest.slackId(),
                userId
        );
    }

    public Slack toEntity(Slack slack) {
        Slack slackBuild = Slack.builder()
                .id(slack.getId())
                .build();

        slackBuild.delete(String.valueOf(userId));

        return slackBuild;
    }
}
