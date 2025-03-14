package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.application.UserFacade;
import com.springcloud.client.user.domain.SignupCommand;
import com.springcloud.client.user.domain.SignupInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    public ResponseEntity<SignupDto.SignupResponse> signup(@RequestBody SignupDto.SignupRequest request) {
        SignupCommand command = request.toCommand();
        SignupInfo info = userFacade.signUp(command);
        SignupDto.SignupResponse response = new SignupDto.SignupResponse(info);
        return ResponseEntity.ok(response);
    }
}
