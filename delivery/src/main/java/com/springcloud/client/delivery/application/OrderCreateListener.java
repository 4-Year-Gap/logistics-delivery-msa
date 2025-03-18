package com.springcloud.client.delivery.application;


import com.springcloud.client.delivery.config.OrderCreateEvent;
import com.springcloud.client.delivery.domain.delivery.Delivery;
import com.springcloud.client.delivery.domain.delivery.DeliveryStatusEnum;
import com.springcloud.client.delivery.infrastructure.client.*;
import com.springcloud.client.delivery.infrastructure.dto.HubClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import com.springcloud.client.delivery.infrastructure.dto.UserInfoClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateListener {

    private final HubClient hubClient;
    private final UserInfoClient userInfoClient;

    @KafkaListener(groupId = "order_create",topics = "order_topic")
    public void createOrderEvent(String orderId){
        log.info("orderI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@d:{}",orderId);
        OrderCreateEvent orderCreateEvent = OrderCreateEvent.fromJson(orderId);

        assert orderCreateEvent != null;

        HubClientResponse<List<HubRoute>> hubClientResponse = hubClient.getRoute(orderCreateEvent.getStartHub(),orderCreateEvent.getEndHub());



        // 업체 배송 담당자.......





        //배송 저장


    }

    private Delivery createDelivery(HubClientResponse hubClientResponse,
                                    UserInfoClientResponse userInfoClientResponse,
                                    OrderCreateEvent orderCreateEvent) {

        return Delivery.create(
                "address",
                DeliveryStatusEnum.ACCEPTED,
                orderCreateEvent.getStartHub(),
                orderCreateEvent.getEndHub(),
                userInfoClientResponse.getSlackId(),
                hubClientResponse.getData()// 어지럽네...
        );
    }
}
