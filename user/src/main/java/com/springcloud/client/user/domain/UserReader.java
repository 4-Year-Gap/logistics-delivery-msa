package com.springcloud.client.user.domain;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserReader {

    Optional<User> findById(UUID userId);

    Optional<User> findByUsername(String username);

    Page<User> findAll(Pageable pageable);

    Page<User> findByUsernameContainingAndDeletedAtIsNull(String keyword, Pageable pageable);
}
