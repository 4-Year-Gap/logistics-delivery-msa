package com.springcloud.client.delivery.domain.delivery;


import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import jakarta.persistence.*;
import lombok.*;
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

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Delivery deliveryId;

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
    @Enumerated(EnumType.STRING)
    @Comment("예상 거리")
    private BigDecimal totalDistance;
    @Comment("실제 시간")
    private LocalTime durationTime;
    @Enumerated(EnumType.STRING)
    @Comment("실제 거리")
    private BigDecimal realDistance;

    @Comment("배송 상태")
    private DeliveryStatusEnum deliveryStatus;



    public static DeliveryHubRoute to(HubRoute hubRoute,UUID destinationHub){
        return DeliveryHubRoute.builder()
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
}
