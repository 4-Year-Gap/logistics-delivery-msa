package com.springcloud.company.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserInfoHeader {

    private final UUID userId;
    private final String username;
    private final String slackId;
    private final UserRole userRole;

    public UserInfoHeader(HttpServletRequest request) {
        this.userId = UUID.fromString(request.getHeader("X-USER-ID"));
        this.username = request.getHeader("X-USERNAME");
        this.slackId = request.getHeader("X-SLACK-ID");
        this.userRole = UserRole.valueOf(request.getHeader("X-USER-ROLE"));
    }
}

