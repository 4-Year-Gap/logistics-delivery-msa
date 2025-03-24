package com.spring_cloud.eureka.client.order.domain.order;


import com.spring_cloud.eureka.client.order.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;


import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_order")
@Entity
@Builder
public class OrderEntity extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(nullable = false, name = "order_Id")
    private UUID orderId;

    @Column(nullable = false)
    @Comment("주문한 사람")
    private UUID orderedBy;

    @Column(nullable = false)
    @Comment("배송 받는 회사 ID")
    private UUID consumeCompanyId;

    @Column(nullable = false)
    @Comment("제품 제공 회사 ID")
    private UUID supplyCompanyId;

    @Column(nullable = false)
    @Comment("제품 ID")
    private UUID productId;

    @Column(nullable = false)
    @Comment("제품 수량")
    private Integer quantity;

    @Column(nullable = false)
    @Comment("제품 총합 가격")
    private Integer totalPrice;

    @Column(nullable = false)
    @Comment("주문 요청 사항")
    private String requestMessage;

    @Enumerated(EnumType.STRING)
    @Comment("배송 현재 상태")
    private OrderEntityStatus status;

    private UUID companyDeliver;


    public static OrderEntity create(UUID userName, UUID productId, Integer productPrice, UUID supplierId, UUID receivingCompanyId, Integer productQuantity, String requestMessage,UUID companyDeliver) {
        return OrderEntity.builder()
                .orderedBy(userName)
                .consumeCompanyId(receivingCompanyId)
                .supplyCompanyId(supplierId)
                .productId(productId)
                .quantity(productQuantity)
                .totalPrice(productPrice)
                .requestMessage(requestMessage)
                .status(OrderEntityStatus.IN_DELIVER)
                .companyDeliver(companyDeliver)
                .build();
    }
}