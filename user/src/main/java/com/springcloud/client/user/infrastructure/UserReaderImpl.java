package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.User;
import com.springcloud.client.user.domain.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findByUsernameContainingAndDeletedAtIsNull(String keyword, Pageable pageable) {
        return userRepository.findByUsernameContainingAndDeletedAtIsNull(keyword, pageable);
    }
}
