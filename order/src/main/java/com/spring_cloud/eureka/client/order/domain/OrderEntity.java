package com.spring_cloud.eureka.client.order.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;


import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_order")
@Entity
public class OrderEntity {

    @Id
    @UuidGenerator
    private UUID orderId;

    private String orderedBy;
    private UUID consumeCompanyId;
    private UUID supplyCompanyId;
    private UUID productId;
    private Integer quantity;
    private Integer totalPrice;
    private String requestMessage;
    private OrderEntityStatus status;

}