package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.domain.SignupCommand;
import com.springcloud.client.user.domain.SignupInfo;
import com.springcloud.client.user.domain.UserRole;
import lombok.Getter;

import java.util.UUID;

public class SignupDto {

    @Getter
    public static class SignupRequest {

        private String username;
        private String password;
        private String slackId;

        public SignupCommand toCommand() {
            return SignupCommand.builder()
                    .username(username)
                    .password(password)
                    .slackId(slackId)
                    .build();
        }
    }

    @Getter
    public static class SignupResponse {

        private final UUID userId;
        private final String username;
        private final String slackId;
        private final UserRole role;

        public SignupResponse(SignupInfo info) {
            this.userId = info.getUserId();
            this.username = info.getUsername();
            this.slackId = info.getSlackId();
            this.role = info.getRole();
        }
    }
}
