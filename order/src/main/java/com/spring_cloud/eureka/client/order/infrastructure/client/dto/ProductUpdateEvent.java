package com.spring_cloud.eureka.client.order.infrastructure.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateEvent implements Serializer {

    private static ObjectMapper objectMapper = new ObjectMapper();

   private UUID productId;
   private Integer quantity;

    public static ProductUpdateEvent create(UUID productId, Integer quantity) {
        return ProductUpdateEvent.builder()
                .productId(productId)
                .quantity(quantity)
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
