package com.springcloud.hub.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "p_hub_routes")
public class HubRoute extends BaseEntity{
    @Id
    @Column(name = "hub_route_id")
    @Comment("허브 라우트 고유키")
    private UUID Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id")
    @Comment("허브 외래키")
    private Hub hub;

    @Column(nullable = false)
    @Comment("출발지 허브 ID")
    private UUID toHubId;

    @Column(nullable = false)
    @Comment("도착지 허브 ID")
    private UUID fromHubId;

    @Column(nullable = false)
    @Comment("소요시간")
    private LocalTime timeRequired;

    @Column(nullable = false, precision = 10, scale = 3)
    @Comment("이동거리")
    private BigDecimal moveDistance;

    @Column(nullable = false)
    @Builder.Default
    @Comment("삭제여부")
    private boolean isDeleted = false;
}
