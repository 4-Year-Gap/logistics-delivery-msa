package com.spring_cloud.eureka.client.order.infrastructure.client.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OrderCreateEvent {
    private UUID orderId;
    private UUID startHub;
    private UUID endHub;
    private UUID productId;
    private Integer productQuantity;

    public static OrderCreateEvent create(UUID orderId, UUID startHub, UUID endHub, UUID productId, Integer productQuantity) {
        return new OrderCreateEvent(
                orderId,
                startHub,
                endHub,
                productId,
                productQuantity
        );
    }


    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
