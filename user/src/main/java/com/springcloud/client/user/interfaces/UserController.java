package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.application.UserFacade;
import com.springcloud.client.user.domain.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
//@RequestMapping("/api/users")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    public ResponseEntity<SignupDto.SignupResponse> signUp(@RequestBody @Valid SignupDto.SignupRequest request) {
        SignupCommand command = request.toCommand();
        SignupInfo info = userFacade.signUp(command);
        SignupDto.SignupResponse response = new SignupDto.SignupResponse(info);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<Void> signIn(@RequestBody SigninDto.SigninRequest request, HttpServletResponse httpServletResponse) {
        SigninCommand command = request.toCommand();
        userFacade.signIn(command, httpServletResponse);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestHeader("X-USER-ID") String userId, @RequestHeader("X-USERNAME") String username, @RequestHeader("X-SLACK-ID") String slackId, @RequestHeader("X-USER-ROLE") String userRole) {
        return ResponseEntity.ok("userId: " + userId + ", username: " + username + ", slackId: " + slackId + ", userRole: " + userRole);
    }

//    @GetMapping("/test2")
//    public ResponseEntity<String> test2(@RequestHeader UserInfoHeader header) {
//        return ResponseEntity.ok("userId: " + header.getUserId() + ", username: " + header.getUsername() + ", slackId: " + header.getSlackId() + ", userRole: " + header.getUserRole());
//    }

    @PostMapping("/hub-manager")
    public ResponseEntity<UserDto.UserResponse> addHubManager(HttpServletRequest httpServletRequest, @RequestBody UserDto.UserRequest request) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        UserCommand command = request.toCommand();
        UserInfo info = userFacade.registerHubManager(userInfoHeader, command);
        UserDto.UserResponse response = new UserDto.UserResponse(info);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<UserDto.UserResponse>> getAllUsers(HttpServletRequest httpServletRequest, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "desc") String order) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        Page<com.springcloud.client.user.domain.UserInfo> pageInfo = userFacade.getAllUsers(userInfoHeader, page, size, sortBy, order);
        Page<UserDto.UserResponse> response = pageInfo.map(UserDto.UserResponse::new);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponse> getUser(HttpServletRequest httpServletRequest, @PathVariable UUID userId) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        UserInfo info = userFacade.getUser(userInfoHeader, userId);
        UserDto.UserResponse response = new UserDto.UserResponse(info);
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<UserDto.UserResponse> updateUser(HttpServletRequest httpServletRequest, @RequestBody UserDto.UserRequest request) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        UserCommand command = request.toCommand();
        UserInfo info = userFacade.updateUser(userInfoHeader, command);
        UserDto.UserResponse response = new UserDto.UserResponse(info);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/delete/{userId}")
    public ResponseEntity<UserDto.UserResponse> deleteUser(HttpServletRequest httpServletRequest, @PathVariable UUID userId) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        UserCommand command = UserCommand.builder().userId(userId).build();
        UserInfo info = userFacade.deleteUser(userInfoHeader, command);
        UserDto.UserResponse response = new UserDto.UserResponse(info);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Page<UserDto.UserResponse>> searchUsers(HttpServletRequest httpServletRequest, @PathVariable String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "desc") String order) {
        UserInfoHeader userInfoHeader = new UserInfoHeader(httpServletRequest);
        Page<UserInfo> pageInfo = userFacade.searchUsers(userInfoHeader, keyword, page, size, sortBy, order);
        Page<UserDto.UserResponse> response = pageInfo.map(UserDto.UserResponse::new);
        return ResponseEntity.ok(response);
    }
}
