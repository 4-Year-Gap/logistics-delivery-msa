package com.springcloud.client.delivery.domain.delivery;


import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
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

    private Integer deliverySequence; // 부산 대전 경기
    private UUID startHub;
    private UUID destinationHub;
    private Integer shipperId;


    @Enumerated(EnumType.STRING)
    private DeliveryStatusEnum deliveryStatus;


    public static DeliveryHubRoute create(HubRoute hubRoute){
        return DeliveryHubRoute.builder()
                .deliverySequence(hubRoute.getDeliverySequence())
                .destinationHub(hubRoute.getDestinationHub())
                .startHub(hubRoute.getStartHub())
                .build();

    }

}
