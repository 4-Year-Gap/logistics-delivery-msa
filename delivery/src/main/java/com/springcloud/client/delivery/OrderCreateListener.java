package com.springcloud.client.delivery;


import com.springcloud.client.delivery.client.HubClient;
import com.springcloud.client.delivery.client.HubRouteRequest;
import com.springcloud.client.delivery.config.OrderCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateListener {

    private final HubClient hubClient;

    @KafkaListener(groupId = "order_create",topics = "order_topic")
    public void createOrderEvent(String orderId){
        log.info("orderI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@d:{}",orderId);
        OrderCreateEvent orderCreateEvent = OrderCreateEvent.fromJson(orderId);

        assert orderCreateEvent != null;
        HubRouteRequest hubRouteRequest = HubRouteRequest.create(orderCreateEvent);







    }
}
