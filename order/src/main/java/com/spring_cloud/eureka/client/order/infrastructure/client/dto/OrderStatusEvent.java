package com.spring_cloud.eureka.client.order.infrastructure.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring_cloud.eureka.client.order.domain.order.OrderEntityStatus;
import lombok.Builder;
import org.apache.kafka.common.serialization.Serializer;

import java.util.UUID;

@Builder
public class OrderStatusEvent implements Serializer {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private UUID  orderId;
    private String status;
    public static OrderStatusEvent create(UUID orderId, OrderEntityStatus orderEntityStatus) {
        return OrderStatusEvent.builder()
                .orderId(orderId)
                .status(orderEntityStatus.name())
                .build();
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
