package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.application.UserFacade;
import com.springcloud.client.user.domain.SigninCommand;
import com.springcloud.client.user.domain.SignupCommand;
import com.springcloud.client.user.domain.SignupInfo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    public ResponseEntity<SignupDto.SignupResponse> signup(@RequestBody @Valid SignupDto.SignupRequest request) {
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

}
