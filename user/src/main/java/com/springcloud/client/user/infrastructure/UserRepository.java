package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Page<User> findByUsernameContainingAndDeletedAtIsNull(String keyword, Pageable pageable);
}