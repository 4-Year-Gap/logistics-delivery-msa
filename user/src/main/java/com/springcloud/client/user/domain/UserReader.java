package com.springcloud.client.user.domain;

import java.util.Optional;

public interface UserReader {

    Optional<User> findByUsername(String username);
}
