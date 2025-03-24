package com.springcloud.client.user.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserInfo {

    private final UUID userId;
    private final String username;
    private final String slackId;
    private final UserRole userRole;
    private final UUID hubId;

    public UserInfo(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.slackId = user.getSlackId();
        this.userRole = user.getRole();
        this.hubId = user.getHubId();
    }
}
