package com.springcloud.client.delivery.application;


import com.springcloud.client.delivery.application.service.DeliveryService;
import com.springcloud.client.delivery.domain.delivery.OrderCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateListener {

    private final DeliveryService deliveryService;


    @KafkaListener(groupId = "order_group",topics = "order-topic")
    public void createOrderEvent(ConsumerRecord<String, OrderCreateEvent> record){

        deliveryService.confirmDelivery(record.value());
    }

}
