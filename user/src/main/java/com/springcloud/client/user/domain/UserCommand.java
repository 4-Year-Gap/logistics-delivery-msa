package com.springcloud.client.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class UserCommand {

    private final UUID userId;
    private final String username;
    private final String slackId;
    private final UserRole userRole;
    private final UUID hubId;
}
