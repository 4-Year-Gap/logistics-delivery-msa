package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.domain.User;
import com.springcloud.client.user.domain.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
