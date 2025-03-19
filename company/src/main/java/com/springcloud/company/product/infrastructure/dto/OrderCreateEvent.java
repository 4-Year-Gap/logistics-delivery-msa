package com.springcloud.company.product.infrastructure.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OrderCreateEvent implements Deserializer {
    private UUID orderId;
    private UUID startHub;
    private UUID endHub;
    private UUID productId;
    private Integer productQuantity;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OrderCreateEvent deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, OrderCreateEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing OrderCreateEvent", e);
        }
    }
}
