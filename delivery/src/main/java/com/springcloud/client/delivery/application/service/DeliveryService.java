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

        return deliveryRepository.search(userId,role,pageable);

    }

    public Delivery getDelivery(Integer userId, String role, UUID deliveryId) {


        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));



        return delivery;
    }

    @Transactional
    public void confirmDelivery(OrderCreateEvent orderCreateEvent) {

        HubClientResponse<List<HubRoute>> hubClientResponse = hubClient.getRoute(orderCreateEvent.getStartHub(),orderCreateEvent.getEndHub());

        DeliveryDriverClientResponse deliveryDriverClientResponse =  deliveryDriverClient.getRoute();
        // 배송 담당자 배정
        Delivery delivery = createDelivery(orderCreateEvent,hubClientResponse,deliveryDriverClientResponse);

        deliveryRepository.save(delivery);
    }

    private Delivery createDelivery(OrderCreateEvent orderCreateEvent, HubClientResponse<List<HubRoute>> hubClientResponse, DeliveryDriverClientResponse deliveryDriverClientResponse) {
        return Delivery.create(
                orderCreateEvent.getAddress(),
                DeliveryStatusEnum.ACCEPTED,
                orderCreateEvent.getStartHub(),
                orderCreateEvent.getEndHub(),
                orderCreateEvent.getReceiverSlackId(),
                createDeliveryHubRoute(hubClientResponse.getData(),deliveryDriverClientResponse)
        );


    }

    private List<DeliveryHubRoute> createDeliveryHubRoute(List<HubRoute> routeList, DeliveryDriverClientResponse deliveryDriverClientResponse) {
        List<HubRoute> sortedRoutes = routeList.stream()
                .sorted(Comparator.comparingInt(HubRoute::getSequenceNumber))
                .toList();

        return IntStream.range(0, sortedRoutes.size())
                .mapToObj(sequence -> {
                    HubRoute currentRoute = sortedRoutes.get(sequence);
                    UUID destinationHubId = (sequence + 1 < sortedRoutes.size()) ? sortedRoutes.get(sequence + 1).getHubId() : currentRoute.getHubId();
                    DeliveryHubRoute hubRoute = DeliveryHubRoute.to(currentRoute, destinationHubId);
                    if (sequence == 0) {
                        hubRoute.changeStatus(DeliveryStatusEnum.WAITING);
                        hubRoute.setShipperId(deliveryDriverClientResponse.getDeliveryDriverId());
                    }
                    return hubRoute;
                })
                .collect(Collectors.toList());
    }

    public void updateDelivery(DeliveryUpdateCommand command) {

        Delivery delivery = deliveryRepository.findById(command.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));

        delivery.setStatus(command.getStatus());
    }

    public void deleteDelivery(DeliveryDeleteCommand command) {

        Delivery delivery = deliveryRepository.findById(command.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));

        delivery.delete(command.getUserName());
    }
}
