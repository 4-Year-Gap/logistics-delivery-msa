package com.spring_cloud.eureka.client.order.domain.order;


import com.spring_cloud.eureka.client.order.infrastructure.client.dto.HubClientResponse;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.ProductClientResponse;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.UserInfoClientResponse;
import com.spring_cloud.eureka.client.order.presentation.dto.request.OrderCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;


import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_order")
@Entity
@Builder
public class OrderEntity {

    @Id
    @UuidGenerator
    @Column(nullable = false, name = "order_Id")
    private UUID orderId;

    @Column(nullable = false)
    @Comment("주문한 사람")
    private String orderedBy;

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
    @Comment("제품 ID")
    private Integer quantity;

    @Column(nullable = false)
    @Comment("제품 총합 가격")
    private Integer totalPrice;

    @Column(nullable = false)
    @Comment("주문 요청 사함")
    private String requestMessage;

    @Enumerated(EnumType.STRING)
    @Comment("배송 현재 상태")
    private OrderEntityStatus status;

    public static OrderEntity create(UserInfoClientResponse userInfoClientResponse, OrderCreateRequest orderCreateRequest) {

        return OrderEntity.builder()
                .orderedBy(userInfoClientResponse.getUserName())
                .consumeCompanyId(orderCreateRequest.getReceivingCompanyId())
                .supplyCompanyId(orderCreateRequest.getSupplierId())
                .productId(orderCreateRequest.getProductId())
                .quantity(orderCreateRequest.getProductQuantity())
                .totalPrice(orderCreateRequest.getProductPrice() * orderCreateRequest.getProductQuantity())
                .requestMessage(orderCreateRequest.getRequestMessage())
                .status(OrderEntityStatus.ACCEPTED)
                .build();

    }
}