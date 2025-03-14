package com.springcloud.client.user.domain;

import lombok.Getter;

@Getter
public class SignupInfo {

    private final Integer userId;
    private final String username;
    private final String slackId;

    public SignupInfo(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.slackId = user.getSlackId();
    }
}