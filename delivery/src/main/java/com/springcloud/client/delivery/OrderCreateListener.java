package com.springcloud.client.delivery;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateListener {

    @KafkaListener(groupId = "order_create",topics = "order_topic")
    public void createOrderEvent(String orderId){
        log.info("orderId:{}",orderId);
    }
}
