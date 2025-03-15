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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Comment("PK")
    private UUID userId;

    @Column(nullable = false, unique = true)
    @Comment("로그인 ID")
    private String username;

    @Column(nullable = false)
    @Comment("비밀번호")
    private String password;

    @Column(nullable = false)
    @Comment("슬랙 Email")
    private String slackId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Comment("사용자 권한")
    private UserRole role;
}
