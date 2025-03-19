package com.springcloud.client.delivery.domain.delivery;


import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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

    private Integer deliverySequence;
    private UUID startHub;
    private UUID destinationHub;
    private UUID shipperId;
    @Enumerated(EnumType.STRING)
    private DeliveryStatusEnum deliveryStatus;



    public static DeliveryHubRoute to(HubRoute hubRoute){
        return DeliveryHubRoute.builder()
                .deliverySequence(hubRoute.getSequenceNumber())
                .startHub(UUID.randomUUID())
                .destinationHub(UUID.randomUUID())
                .deliveryStatus(DeliveryStatusEnum.NOT_ACCEPTED)
                .build();


    }

    public void setShipperId(UUID shipperId) {
        this.shipperId = shipperId;
    }

    public void changeStatusToAccept() {
        this.deliveryStatus = DeliveryStatusEnum.ACCEPTED;
    }
}
