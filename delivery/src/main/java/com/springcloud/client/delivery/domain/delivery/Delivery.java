package com.springcloud.client.delivery.domain.delivery;



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

    @Comment("주문 ID")
    private UUID orderId;

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

    @Comment("업체 배송 담당자")
    @Column(nullable = true)
    private UUID companyDeliver;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryHubRoute> deliveryHubRouteList;

    public static Delivery create(String address,
                                  DeliveryStatusEnum deliveryStatusEnum,
                                  UUID startHub,
                                  UUID endHub,
                                  String slackId,
                                  UUID receiverId,
                                  UUID orderId,
                                  UUID companyDeliver
    )
    {
        return Delivery.builder()
                .address(address)
                .status(deliveryStatusEnum)
                .startHubId(startHub)
                .endHubId(endHub)
                .receiverSlackId(slackId)
                .receiverId(receiverId)
                .orderId(orderId)
                .companyDeliver(companyDeliver)
                .build();

    }
    public void updateStatus(DeliveryStatusEnum status){
        this.status = status;
    }
}
