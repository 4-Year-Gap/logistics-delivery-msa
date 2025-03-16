package com.spring_cloud.eureka.client.order.domain.delivery;


import com.spring_cloud.eureka.client.order.domain.BaseEntity;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.HubClientResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_ deliveries")
public class Delivery extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(nullable = false, name = "delivery_Id")
    private UUID deliveryId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("배송 상태")
    private DeliveryStatusEnum status;

    @Column(nullable = false)
    @Comment("배송 시작 허브")
    private UUID startHubId;

    @Column(nullable = false)
    @Comment("배송 마지막 허브")
    private UUID endHubId;

    @Column(nullable = false)
    @Comment("배송지 주소")
    @Length(max = 100)
    private String address;

    @Column(nullable = false)
    @Comment("배송 받는 사람 슬랙 ID")
    private String receiverSlackId;

    @Column(nullable = false)
    @Comment("배송 받는 사람 ID")
    private UUID receiverId;

    @OneToMany(mappedBy = "deliveryId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryHubRoute> deliveryHubRouteList;

    public static Delivery create(String address,
                                  DeliveryStatusEnum deliveryStatusEnum,
                                  UUID startHub, UUID endHub,
                                  String slackId,
                                  UUID id,
                                  List<DeliveryHubRoute> deliveryHubRouteList)
    {
        return Delivery.builder()
                .address(address)
                .status(deliveryStatusEnum)
                .startHubId(startHub)
                .endHubId(endHub)
                .receiverSlackId(slackId)
                .deliveryHubRouteList(deliveryHubRouteList)
                .build();

    }
}
