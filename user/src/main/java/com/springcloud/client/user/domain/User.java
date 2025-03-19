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
}
