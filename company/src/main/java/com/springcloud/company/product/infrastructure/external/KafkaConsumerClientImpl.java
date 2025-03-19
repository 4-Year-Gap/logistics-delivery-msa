package com.springcloud.company.product.infrastructure.external;

import com.springcloud.company.product.infrastructure.dto.OrderCreateEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerClientImpl {

    @KafkaListener(groupId = "order_group", topics = "order-topic")
    public void listen(ConsumerRecord<String, OrderCreateEvent> record) {
        System.out.println("Received message: " + record.value());
    }
}
