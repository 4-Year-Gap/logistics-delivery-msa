package com.springcloud.client.delivery.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateEvent implements Deserializer<OrderCreateEvent> {


    private static final ObjectMapper objectMapper = new ObjectMapper();

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
    @Override
    public OrderCreateEvent deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.readValue(data, OrderCreateEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing OrderCreateEvent", e);
        }
    }

}