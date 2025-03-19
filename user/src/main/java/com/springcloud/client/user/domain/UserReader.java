package com.springcloud.client.user.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserReader {

    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID userId);
}
