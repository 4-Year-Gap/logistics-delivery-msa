package com.springcloud.client.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "p_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Comment("사용자 식별자")
    private UUID userId;

    @Column(nullable = false, unique = true)
    @Comment("로그인 아이디")
    private String username;

    @Column(nullable = false)
    @Comment("로그인 비밀번호")
    private String password;

    @Column(nullable = false)
    @Comment("슬랙 이메일")
    private String slackId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Comment("사용자 권한")
    private UserRole role;

    @Column(nullable = true)
    @Comment("허브 관리자의 소속 허브 ID (role이 HUB_MANAGER인 경우)")
    private UUID hubId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = true)
    private DeliveryDriver deliveryDriver;

    public void update(UserCommand userCommand) {
        if (userCommand.getUsername() != null) this.username = userCommand.getUsername();
        if (userCommand.getSlackId() != null) this.slackId = userCommand.getSlackId();
        if (userCommand.getUserRole() != null) this.role = userCommand.getUserRole();
        if (userCommand.getHubId() != null) {
            this.hubId = userCommand.getHubId();
            this.role = UserRole.HUB_MANAGER;
        }
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateSlackId(String slackId) {
        this.slackId = slackId;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }
}
