package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.domain.UserCommand;
import com.springcloud.client.user.domain.UserInfo;
import com.springcloud.client.user.domain.UserRole;
import lombok.Getter;

import java.util.UUID;

public class UserDto {

    @Getter
    public static class UserRequest {

        private UUID userId;
        private String username;
        private String slackId;
        private UserRole userRole;
        private UUID hubId;

        public UserCommand toCommand() {
            return UserCommand.builder()
                    .userId(this.userId)
                    .username(this.username)
                    .slackId(this.slackId)
                    .userRole(this.userRole)
                    .hubId(this.hubId)
                    .build();
        }
    }

    @Getter
    public static class UserResponse {

        private final UUID userId;
        private final String username;
        private final String slackId;
        private final UserRole userRole;
        private final UUID hubId;

        public UserResponse(UserInfo userInfo) {
            this.userId = userInfo.getUserId();
            this.username = userInfo.getUsername();
            this.slackId = userInfo.getSlackId();
            this.userRole = userInfo.getUserRole();
            this.hubId = userInfo.getHubId();
        }
    }
}
