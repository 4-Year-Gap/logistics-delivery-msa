package com.springcloud.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Table(name = "p_hubs")
public class Hub extends BaseEntity {
    @Id
    @Column(name = "hub_id")
    @Comment("허브 고유키")
    private UUID Id;

    @Column(nullable = false)
    @Comment("허브명")
    private String name;

    @Column(nullable = false)
    @Comment("허브주소")
    private String address;

    @Column(nullable = false, precision = 10, scale = 7)
    @Comment("위도")
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    @Comment("경도")
    private BigDecimal longitude;

    @Column(name = "user_id")
    @Comment("허브 담당자 ID")
    private UUID userId;
}
