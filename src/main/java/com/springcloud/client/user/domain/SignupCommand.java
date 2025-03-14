package com.springcloud.client.user.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignupCommand {

    private String username;
    private String password;
    private String slackId;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .slackId(slackId)
                .role(UserRole.MASTER)
                .build();
    }
}
