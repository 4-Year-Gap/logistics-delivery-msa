package com.springcloud.client.user.application;

import com.springcloud.client.user.domain.SigninCommand;
import com.springcloud.client.user.domain.SignupCommand;
import com.springcloud.client.user.domain.SignupInfo;
import com.springcloud.client.user.domain.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public SignupInfo signUp(SignupCommand command) {
        return userService.signUp(command);
    }

    public void signIn(SigninCommand command, HttpServletResponse httpServletResponse) {
        userService.signIn(command, httpServletResponse);
    }
}
