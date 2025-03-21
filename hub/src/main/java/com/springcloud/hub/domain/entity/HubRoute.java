package com.springcloud.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Table(name = "p_hub_routes")
public class HubRoute extends BaseEntity {
    @Id
    @Column(name = "hub_route_id")
    @Comment("허브 라우트 고유키")
    private UUID Id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "to_hub_id", referencedColumnName = "hub_id", nullable = false)
    @Comment("출발지 허브 ID")
    private Hub toHub;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "from_hub_id", referencedColumnName = "hub_id", nullable = false)
    @Comment("도착지 허브 ID")
    private Hub fromHub;

    @Column(nullable = false)
    @Comment("소요시간")
    private LocalTime timeRequired;

    @Column(nullable = false, precision = 10, scale = 3)
    @Comment("이동거리")
    private BigDecimal moveDistance;
}
