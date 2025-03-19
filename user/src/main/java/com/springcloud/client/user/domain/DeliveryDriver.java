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
@Table(name = "p_delivery_driver")
public class DeliveryDriver extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Comment("PK")
    private UUID deliveryDriverId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Comment("소속 허브 ID (허브 배송 기사인 경우 null)")
    @Column(nullable = true)
    private UUID hubId;

    @Comment("배송 순번 (0부터 시작, 순차 배정)")
    @Column(nullable = false)
    private int deliveryOrderNumber;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Comment("배송 기사 권한")
    private DeliveryDriverRole role;
}
