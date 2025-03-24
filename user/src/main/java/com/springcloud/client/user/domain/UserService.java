package com.springcloud.client.user.domain;

import com.springcloud.client.user.interfaces.UserInfoHeader;
import com.springcloud.client.user.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final List<Integer> ALLOWED_PAGE_SIZES = List.of(10, 30, 50);
    private static final int DEFAULT_PAGE_SIZE = 10;

    public SignupInfo signUp(SignupCommand command) {
        Optional<User> checkUsername = userReader.findByUsername(command.getUsername());

        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }

        String encodedPassword = passwordEncoder.encode(command.getPassword());
        User user = command.toEntity(encodedPassword);
        User savedUser = userStore.save(user);

        return new SignupInfo(savedUser);
    }

    public void signIn(SigninCommand command, HttpServletResponse httpServletResponse) {
        User user = userReader.findByUsername(command.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 사용자입니다."));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT를 생성하고 쿠키에 저장한 후 HttpServletResponse에 추가하여 반환
        String token = jwtUtil.createToken(user);
        jwtUtil.addJwtToCookie(token, httpServletResponse);
    }

    public Page<UserInfo> getAllUsers(UserInfoHeader header, int page, int size, String sortBy, String order) {
        if (header.getUserRole() != UserRole.MASTER) {
            throw new IllegalArgumentException("전체 사용자 조회 권한이 없습니다.");
        }

        if (!ALLOWED_PAGE_SIZES.contains(size)) {   // 허용된 페이지 사이즈가 아닌 경우, 기본 페이지 사이즈로 설정
            size = DEFAULT_PAGE_SIZE;
        }

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return userReader.findAll(pageable).map(UserInfo::new);
    }

    public UserInfo getUser(UserInfoHeader userInfoHeader, UUID userId) {
        if (userInfoHeader.getUserId() != userId && userInfoHeader.getUserRole() != UserRole.MASTER) {
            throw new IllegalArgumentException("다른 사용자의 정보를 조회할 수 있는 권한이 없습니다.");
        }

        return userReader.findById(userId)
                .map(UserInfo::new)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public UserInfo updateUser(UserInfoHeader userInfoHeader, UserCommand command) {
        if (userInfoHeader.getUserRole() != UserRole.MASTER) {
            throw new IllegalArgumentException("다른 사용자의 정보를 수정할 수 있는 권한이 없습니다.");
        }

        User user = userReader.findById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.update(command);

        return new UserInfo(user);
    }

    @Transactional
    public UserInfo deleteUser(UserInfoHeader userInfoHeader, UserCommand command) {
        if (userInfoHeader.getUserRole() != UserRole.MASTER) {
            throw new IllegalArgumentException("다른 사용자의 정보를 삭제할 수 있는 권한이 없습니다.");
        }

        User user = userReader.findById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.delete();

        return new UserInfo(user);
    }

    public Page<UserInfo> searchUsers(UserInfoHeader userInfoHeader, String keyword, int page, int size, String sortBy, String order) {
        if (userInfoHeader.getUserRole() != UserRole.MASTER) {
            throw new IllegalArgumentException("사용자 검색 권한이 없습니다.");
        }

        if (!ALLOWED_PAGE_SIZES.contains(size)) {   // 허용된 페이지 사이즈가 아닌 경우, 기본 페이지 사이즈로 설정
            size = DEFAULT_PAGE_SIZE;
        }

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return userReader.findByUsernameContainingAndDeletedAtIsNull(keyword, pageable).map(UserInfo::new);
    }

    @Transactional
    public UserInfo registerHubManager(UserInfoHeader userInfoHeader, UserCommand command) {
        if (userInfoHeader.getUserRole() != UserRole.MASTER) {
            throw new IllegalArgumentException("허브 관리자 등록 권한이 없습니다.");
        }

        User user = userReader.findById(command.getUserId())
                .filter(u -> u.getDeletedAt() == null) // 삭제되지 않은 사용자 필터링
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없거나 삭제된 사용자입니다."));

        user.update(command);

        return new UserInfo(user);
    }
}