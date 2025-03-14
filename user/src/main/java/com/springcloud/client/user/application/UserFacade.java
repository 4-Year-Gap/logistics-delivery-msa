package com.springcloud.client.user.application;

import com.springcloud.client.user.domain.SignupCommand;
import com.springcloud.client.user.domain.SignupInfo;
import com.springcloud.client.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public SignupInfo signUp(SignupCommand command) {
        return userService.signUp(command);
    }
}
