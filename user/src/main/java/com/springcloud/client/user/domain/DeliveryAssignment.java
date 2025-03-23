package com.springcloud.client.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "p_delivery_assignment")
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK")
    private Byte deliveryAssignmentId;

    @Column(nullable = false)
    @Comment("현재 배송 담당자 인덱스")
    private Integer currentDriverIndex;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Comment("배송 기사 역할 (허브 or 업체)")
    private DeliveryDriverRole driverType;

    public void updateCurrentDriverIndex(int newIndex) {
        this.currentDriverIndex = newIndex;
    }
}