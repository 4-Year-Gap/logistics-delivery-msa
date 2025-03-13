package com.spring_cloud.eureka.client.order.domain.order;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;


import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_order")
@Entity
public class OrderEntity {

    @Id
    @UuidGenerator
    @Column(nullable = false, name = "order_Id")
    private UUID orderId;

    @Column(nullable = false)
    private String orderedBy;
    @Column(nullable = false)
    private UUID consumeCompanyId;
    @Column(nullable = false)
    private UUID supplyCompanyId;
    @Column(nullable = false)
    private UUID productId;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Integer totalPrice;
    @Column(nullable = false)
    private String requestMessage;
    @Enumerated(EnumType.STRING)
    private OrderEntityStatus status;

}