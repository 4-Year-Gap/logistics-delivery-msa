package com.springcloud.client.delivery.application;


import com.springcloud.client.delivery.client.*;
import com.springcloud.client.delivery.config.OrderCreateEvent;
import com.springcloud.client.delivery.delivery.Delivery;
import com.springcloud.client.delivery.delivery.DeliveryStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
        HubRouteRequest hubRouteRequest = HubRouteRequest.create(orderCreateEvent);

        HubClientResponse hubClientResponse = hubClient.getRoute(hubRouteRequest).data();

        UserInfoClientResponse userInfoClientResponse = userInfoClient.getUserInfo(1).data();

        assert hubClientResponse != null;
        assert userInfoClientResponse != null;

        Delivery delivery = createDelivery(hubClientResponse,userInfoClientResponse);



        //배송 저장


    }

    private Delivery createDelivery(HubClientResponse hubClientResponse,UserInfoClientResponse userInfoClientResponse) {

        return Delivery.create(
                "address",
                DeliveryStatusEnum.ACCEPTED,
                hubClientResponse.getShortestRoute().get(0).getStartHub(),
                hubClientResponse.getShortestRoute().get(0).getDestinationHub(),
                userInfoClientResponse.getSlackId(),
                hubClientResponse.fromHubRoute()
        );
    }
}
