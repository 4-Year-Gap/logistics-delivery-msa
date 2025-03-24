package com.springcloud.client.user.application;

import com.springcloud.client.user.domain.*;
import com.springcloud.client.user.interfaces.UserInfoHeader;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public UserInfo registerHubManager(UserInfoHeader userInfoHeader, UserCommand command) {
        return userService.registerHubManager(userInfoHeader, command);
    }

    public Page<UserInfo> getAllUsers(UserInfoHeader header, int page, int size, String sortBy, String order) {
        return userService.getAllUsers(header, page, size, sortBy, order);
    }

    public UserInfo getUser(UserInfoHeader userInfoHeader, UUID userId) {
        return userService.getUser(userInfoHeader, userId);
    }

    public UserInfo updateUser(UserInfoHeader userInfoHeader, UserCommand command) {
        return userService.updateUser(userInfoHeader, command);
    }

    public UserInfo deleteUser(UserInfoHeader userInfoHeader, UserCommand command) {
        return userService.deleteUser(userInfoHeader, command);
    }

    public Page<UserInfo> searchUsers(UserInfoHeader userInfoHeader, String keyword, int page, int size, String sortBy, String order) {
        return userService.searchUsers(userInfoHeader, keyword, page, size, sortBy, order);
    }
}
