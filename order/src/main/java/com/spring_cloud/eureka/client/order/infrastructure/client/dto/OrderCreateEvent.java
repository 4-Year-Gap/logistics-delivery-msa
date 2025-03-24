package com.spring_cloud.eureka.client.order.infrastructure.client.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class OrderCreateEvent implements Serializer {

    private static ObjectMapper objectMapper = new ObjectMapper();


    private UUID orderId;
    private UUID startHub;
    private UUID endHub;
    private UUID productId;
    private Integer productQuantity;
    private String receiverSlackId;
    private String address;
    private UUID receiverId;
    private UUID userId;
    private UUID companyDeliver;

    public static OrderCreateEvent create(UUID orderId,
                                          UUID startHub,
                                          UUID endHub,
                                          UUID productId,
                                          Integer productQuantity,
                                          String receiverSlackId,
                                          String address,
                                          UUID receiverId,
                                          UUID userId,
                                          UUID companyDeliver
                                          ) {
        return new OrderCreateEvent(
                orderId,
                startHub,
                endHub,
                productId,
                productQuantity,
                receiverSlackId,
                address,
                receiverId,
                userId,
                companyDeliver
        );
    }

    @Override
    public byte[] serialize(String s, Object data) {

        try {
            if (data == null) {
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing OrderCreateEvent", e);
        }
    }
}
