package com.springcloud.client.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;

    public SignupInfo signUp(SignupCommand command) {

        Optional<User> checkUsername = userReader.findByUsername(command.getUsername());
        if (checkUsername.isPresent()) throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");

        String encodedPassword = passwordEncoder.encode(command.getPassword());
        User user = command.toEntity(encodedPassword);
        User savedUser = userStore.save(user);

        return new SignupInfo(savedUser);
    }
}