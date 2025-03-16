package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.domain.SigninCommand;
import lombok.Getter;

public class SigninDto {
    @Getter
    public static class SigninRequest {

        private String username;
        private String password;

        public SigninCommand toCommand() {
            return SigninCommand.builder()
                    .username(username)
                    .password(password)
                    .build();
        }
    }
}
