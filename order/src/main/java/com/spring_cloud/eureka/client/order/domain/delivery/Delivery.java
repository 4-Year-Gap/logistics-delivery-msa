package com.spring_cloud.eureka.client.order.domain.delivery;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatusEnum status;

    @Column(nullable = false)
    private UUID startHubId;

    @Column(nullable = false)
    private UUID endHubId;

    @Column(nullable = false)
    private String address;

    private String receiverSlackId;

    @Column(nullable = false)
    private UUID receiverId;

    public Delivery create(){
        return Delivery.builder()

                .build();
    }
}
