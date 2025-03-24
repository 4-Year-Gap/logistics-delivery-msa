package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.domain.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserInfoHeader {

    private final UUID userId;
    private final String username;
    private final String slackId;
    private final UserRole userRole;
    private final UUID hubId;

    public UserInfoHeader(HttpServletRequest request) {
        this.userId = UUID.fromString(request.getHeader("X-USER-ID"));
        this.username = request.getHeader("X-USERNAME");
        this.slackId = request.getHeader("X-SLACK-ID");
        this.userRole = UserRole.valueOf(request.getHeader("X-USER-ROLE"));
        this.hubId = UUID.fromString(request.getHeader("X-HUB-ID"));
    }
}
