package com.springcloud.hub.infrastructure.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityIntegrationCommand implements Serializer {

    private UUID userId;
    private UUID hubId;
    private UUID companyId;
    private UUID orderId;
    private UUID deliveryId;

    @Override
    public byte[] serialize(String s, Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (o == null) {
                return new byte[0];
            }
            return objectMapper.writeValueAsBytes(o);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize JSON", e);
        }
    }
}
