package com.springcloud.client.delivery.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateEvent {
    private UUID orderId;
    private UUID startHub;
    private UUID endHub;
    private UUID productId;
    private Integer productQuantity;

    @Override
    public String toString() {
        return "OrderCreateEvent{" +
                "orderId='" + orderId + '\'' +
                ", startHub=" + startHub +
                ", endHub=" + endHub +
                '}';
    }

    public static OrderCreateEvent fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, OrderCreateEvent.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
