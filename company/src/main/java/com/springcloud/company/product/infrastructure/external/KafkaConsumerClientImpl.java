package com.springcloud.company.product.infrastructure.external;

import com.springcloud.company.product.infrastructure.dto.OrderCreateEvent;
import com.springcloud.company.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaConsumerClientImpl {

    private final ProductService productService;

    @KafkaListener(groupId = "order_group", topics = "order-topic")
    public void listen(ConsumerRecord<String, OrderCreateEvent> record) {
        System.out.printf("Received message: %s%n", record.value());
        productService.updateStock(record.value());
    }


}
