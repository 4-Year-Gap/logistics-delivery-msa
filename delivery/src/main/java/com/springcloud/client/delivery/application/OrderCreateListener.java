package com.springcloud.client.delivery.application;


import com.springcloud.client.delivery.application.service.DeliveryService;
import com.springcloud.client.delivery.domain.delivery.OrderCreateEvent;
import com.springcloud.client.delivery.domain.delivery.OrderStatusEvent;
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

        log.info(record.value().getAddress());
        log.info(record.value().getReceiverSlackId());


        deliveryService.confirmDelivery(record.value());
    }

    @KafkaListener(groupId = "test", topics = "order-status-topic")
    public void updateStatusOrderEvent(ConsumerRecord<String, OrderStatusEvent> record){

        deliveryService.updateDeliveryEvent(record.value());
    }

}
