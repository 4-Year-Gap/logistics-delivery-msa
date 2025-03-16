package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.domain.SignupCommand;
import com.springcloud.client.user.domain.SignupInfo;
import com.springcloud.client.user.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;



import java.util.UUID;

public class SignupDto {

    @Getter
    public static class SignupRequest {

        @NotBlank(message = "Username은 필수 입력값입니다.")
        @Size(min = 4, max = 10, message = "Username은 최소 4자 이상, 10자 이하여야 합니다.")
        @Pattern(regexp = "^[a-z0-9]+$", message = "Username은 알파벳 소문자(a~z)와 숫자(0~9)만 포함할 수 있습니다.")
        private String username;

        @NotBlank(message = "Password는 필수 입력값입니다.")
        @Size(min = 8, max = 15, message = "Password는 최소 8자 이상, 15자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password는 알파벳 대소문자(A~Z, a~z), 숫자(0~9), 특수문자를 포함해야 합니다.")
        private String password;

        @NotBlank(message = "Email은 필수 입력값입니다.")
        @Email(message = "올바른 이메일 형식을 입력해 주세요.")
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
