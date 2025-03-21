package com.springcloud.client.delivery.application.service;


import com.springcloud.client.delivery.domain.delivery.*;
//import com.springcloud.client.delivery.infrastructure.client.DeliveryDriverClient;
import com.springcloud.client.delivery.infrastructure.client.HubClient;
import com.springcloud.client.delivery.infrastructure.client.UserClient;
import com.springcloud.client.delivery.infrastructure.dto.DeliveryDriverClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import com.springcloud.client.delivery.infrastructure.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubClient hubClient;
//    private final DeliveryDriverClient deliveryDriverClient;
    private final UserClient userInfoClient;


    public Page<Delivery> getDeliveries(Integer userId, String role, Pageable pageable) {

        //N + 1 문제 해결 필요
        return deliveryRepository.search(userId,role,pageable);

    }

    public Delivery getDelivery(Integer userId, String role, UUID deliveryId) {


        //N + 1 문제 해결 필요
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));



        return delivery;
    }

    @Transactional
    public void confirmDelivery(OrderCreateEvent orderCreateEvent) {


        HubClientResponse<List<HubRoute>> hubClientResponse = hubClient.getRoute(orderCreateEvent.getStartHub(),orderCreateEvent.getEndHub());
        DeliveryDriverClientResponse deliveryDriverClientResponse = userInfoClient.getRoute();

        // 배송 담당자 배정
        Delivery delivery = createDelivery(orderCreateEvent,hubClientResponse,deliveryDriverClientResponse);

        deliveryRepository.save(delivery);
    }

    private Delivery createDelivery(OrderCreateEvent orderCreateEvent, HubClientResponse<List<HubRoute>> hubClientResponse, DeliveryDriverClientResponse deliveryDriverClientResponse) {


        return Delivery.create(
                orderCreateEvent.getAddress(),
                DeliveryStatusEnum.WAITING,
                orderCreateEvent.getStartHub(),
                orderCreateEvent.getEndHub(),
                orderCreateEvent.getReceiverSlackId(),
                createDeliveryHubRoute(hubClientResponse.getData(),deliveryDriverClientResponse),
                orderCreateEvent.getUserId()
        );
    }

    private List<DeliveryHubRoute> createDeliveryHubRoute(List<HubRoute> routeList, DeliveryDriverClientResponse deliveryDriverClientResponse) {

        List<HubRoute> sortedRoutes = routeList.stream()
                .sorted(Comparator.comparingInt(HubRoute::getSequenceNumber))
                .toList();
        return IntStream.range(0, sortedRoutes.size() - 1)
                .mapToObj(i -> {
                    HubRoute currentRoute = sortedRoutes.get(i);
                    HubRoute nextRoute = sortedRoutes.get(i + 1);
                    DeliveryHubRoute hubRoute = DeliveryHubRoute.to(currentRoute, nextRoute.getHubId());
                    if (i == 0) {
                        hubRoute.changeStatus(DeliveryStatusEnum.WAITING);
                        hubRoute.setShipperId(deliveryDriverClientResponse.getDeliveryDriverId());
                    }
                    return hubRoute;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateDelivery(DeliveryUpdateCommand command) {

        Delivery delivery = deliveryRepository.findById(command.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));

        delivery.setStatus(command.getStatus());


        if(command.getArrivedHub().equals(delivery.getEndHubId()) && command.getStatus().equals(DeliveryStatusEnum.ACCEPTED)){

            delivery.setStatus(DeliveryStatusEnum.IN_DELIVER);

            designationCompanyDeliver(delivery);
        }
    }

    private void designationCompanyDeliver(Delivery delivery) {
        // 업체 배송 담당자 지정 로직
    }

    @Transactional
    public void deleteDelivery(DeliveryDeleteCommand command) {

        Delivery delivery = deliveryRepository.findById(command.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));

        delivery.delete(command.getUserName());
    }
}
