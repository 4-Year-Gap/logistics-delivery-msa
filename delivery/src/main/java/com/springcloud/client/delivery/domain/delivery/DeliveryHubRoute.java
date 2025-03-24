package com.springcloud.client.delivery.domain.delivery;


import com.springcloud.client.delivery.infrastructure.dto.HubRoute;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_ delivery_routes")
public class DeliveryHubRoute {


    @Id
    @UuidGenerator
    @Column(nullable = false, name = "route_id")
    private UUID routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;
    @Comment("배송 순번")
    private Integer deliverySequence;
    @Comment("시작 허브")
    private UUID startHub;
    @Comment("도착지 허브")
    private UUID destinationHub;
    @Comment("허브 배송 담당자 ID")
    private UUID shipperId;
    @Comment("예상 시간")
    private LocalTime timeRequired;

    @Comment("예상 거리")
    private BigDecimal totalDistance;
    @Comment("실제 시간")
    private LocalTime durationTime;

    @Comment("실제 거리")
    private BigDecimal realDistance;

    @Enumerated(EnumType.STRING)
    @Comment("배송 상태")
    private DeliveryStatusEnum deliveryStatus;



    public static DeliveryHubRoute to(HubRoute hubRoute,UUID destinationHub,Delivery delivery){
        return DeliveryHubRoute.builder()
                .delivery(delivery)
                .deliverySequence(hubRoute.getSequenceNumber())
                .startHub(hubRoute.getHubId())
                .destinationHub(destinationHub)
                .totalDistance(hubRoute.getTotalDistance())
                .timeRequired(hubRoute.getTimeRequired())
                .deliveryStatus(DeliveryStatusEnum.NOT_ACCEPTED)
                .build();
    }

    public void setShipperId(UUID shipperId) {
        this.shipperId = shipperId;
    }

    public void changeStatus(DeliveryStatusEnum deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void updateDeliveryStatus(DeliveryStatusEnum deliveryStatusEnum) {
        this.deliveryStatus = deliveryStatusEnum;
    }
}
