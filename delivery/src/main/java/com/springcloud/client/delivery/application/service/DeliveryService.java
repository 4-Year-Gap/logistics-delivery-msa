package com.springcloud.client.delivery.application.service;


import com.springcloud.client.delivery.domain.delivery.*;
import com.springcloud.client.delivery.infrastructure.client.DeliveryDriverClient;
import com.springcloud.client.delivery.infrastructure.client.HubClient;
import com.springcloud.client.delivery.infrastructure.dto.DeliveryDriverClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import com.springcloud.client.delivery.infrastructure.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubClient hubClient;
    private final DeliveryDriverClient deliveryDriverClient;


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
        DeliveryDriverClientResponse deliveryDriverClientResponse =  deliveryDriverClient.getRoute();
        /*
         * HubClientResponse 를 통해 최단 경로 허브 루트를 알아오기
         * DeliveryDriverClientResponse 를 통해 현재 배송이 가능한 허브 배송 담당자 정보 가지고 오기
         * Delivery 생성 및 저장
         */

        // 배송 담당자 배정
        Delivery delivery = createDelivery(orderCreateEvent,hubClientResponse,deliveryDriverClientResponse);

        deliveryRepository.save(delivery);
    }

    private Delivery createDelivery(OrderCreateEvent orderCreateEvent, HubClientResponse<List<HubRoute>> hubClientResponse, DeliveryDriverClientResponse deliveryDriverClientResponse) {

        /*
         * 배송을 생성 처음 상태는 대기중
         */
        return Delivery.create(
                orderCreateEvent.getAddress(),
                DeliveryStatusEnum.WAITING,
                orderCreateEvent.getStartHub(),
                orderCreateEvent.getEndHub(),
                orderCreateEvent.getReceiverSlackId(),
                createDeliveryHubRoute(hubClientResponse.getData(),deliveryDriverClientResponse)
        );
    }

    private List<DeliveryHubRoute> createDeliveryHubRoute(List<HubRoute> routeList, DeliveryDriverClientResponse deliveryDriverClientResponse) {
        // 리스트 순서대로 정렬
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

        //만약 마지막 허브에 도착을 한다면
        if(command.getArrivedHub().equals(delivery.getEndHubId()) && command.getStatus().equals(DeliveryStatusEnum.ACCEPTED)){
            // 업체 배송 담당자 지정
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
