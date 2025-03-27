package com.springcloud.client.delivery.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.serialization.Deserializer;


import java.util.UUID;

@Getter
@Builder
public class OrderStatusEvent implements Deserializer<OrderStatusEvent> {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private UUID  orderId;
    private String status;


    @Override
    public OrderStatusEvent deserialize(String s, byte[] data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.readValue(data, OrderStatusEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing OrderCreateEvent", e);
        }
    }
}
