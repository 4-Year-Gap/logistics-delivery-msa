package com.springcloud.client.user.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SignupInfo {

    private final UUID userId;
    private final String username;
    private final String slackId;
    private final UserRole role;

    public SignupInfo(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.slackId = user.getSlackId();
        this.role = user.getRole();
    }
}