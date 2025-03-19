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
    private Integer deliveryAssignmentId;

    @Column(nullable = false)
    @Comment("현재 배송 담당자 인덱스")
    private int currentDriverIndex;

    public void updateCurrentDriverIndex(int newIndex) {
        this.currentDriverIndex = newIndex;
    }
}
