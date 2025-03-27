package com.springcloud.client.delivery.application.service;


import com.springcloud.client.delivery.application.*;
import com.springcloud.client.delivery.domain.delivery.*;
//import com.springcloud.client.delivery.infrastructure.client.DeliveryDriverClient;
import com.springcloud.client.delivery.infrastructure.client.HubClient;
import com.springcloud.client.delivery.infrastructure.client.UserClient;
import com.springcloud.client.delivery.infrastructure.dto.DeliveryDriverClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubClientResponse;
import com.springcloud.client.delivery.infrastructure.dto.HubRoute;
import com.springcloud.client.delivery.infrastructure.repository.DeliveryHubRouteRepository;
import com.springcloud.client.delivery.infrastructure.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryHubRouteRepository deliveryHubRouteRepository;

    private final HubClient hubClient;
    private final UserClient userInfoClient;
    private final KafkaTemplate<String, IdentityIntegrationDTO> updateKafkaTemplate;


    public Page<Delivery> getDeliveries(UUID userId, String role, Pageable pageable,UUID hubId) {

        return deliveryRepository.search(userId,role,pageable,hubId);
    }

    public Delivery getDelivery(Integer userId, String role, UUID deliveryId) {


        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public void confirmDelivery(OrderCreateEvent orderCreateEvent) {


        HubClientResponse<List<HubRoute>> hubClientResponse = hubClient.getRoute(orderCreateEvent.getStartHub(),orderCreateEvent.getEndHub());

        if(hubClientResponse.getData() == null){
            throw new IllegalArgumentException("허브 루트 정보를 받아오는데 실패하였습니다");
        }

        DeliveryDriverClientResponse deliveryDriverClientResponse = userInfoClient.getRoute();


        if(deliveryDriverClientResponse == null){
            throw new IllegalArgumentException("배송자 정보를 받아오는데 실패하였습니다");
        }

        Delivery delivery = deliveryRepository.save(createDelivery(orderCreateEvent));

        createDeliveryHubRoute(hubClientResponse.getData(),deliveryDriverClientResponse,delivery);


        //

        IdentityIntegrationDTO identityIntegrationDTO = IdentityIntegrationDTO.builder()
                .userId(deliveryDriverClientResponse.getDeliveryDriverId())
                .orderId(orderCreateEvent.getOrderId())
                .build();

        updateKafkaTemplate.send("integrated-user-topic",identityIntegrationDTO);
    }

    @Transactional
    protected Delivery createDelivery(OrderCreateEvent orderCreateEvent) {

        return Delivery.create(
                orderCreateEvent.getAddress(),
                DeliveryStatusEnum.WAITING,
                orderCreateEvent.getStartHub(),
                orderCreateEvent.getEndHub(),
                orderCreateEvent.getReceiverSlackId(),
                orderCreateEvent.getReceiverId(),
                orderCreateEvent.getOrderId(),
                orderCreateEvent.getCompanyDeliver()
        );
    }

    @Transactional
    protected List<DeliveryHubRoute> createDeliveryHubRoute(List<HubRoute> routeList, DeliveryDriverClientResponse deliveryDriverClientResponse,Delivery delivery) {

        List<HubRoute> sortedRoutes = routeList.stream()
                .sorted(Comparator.comparingInt(HubRoute::getSequenceNumber))
                .toList();
        List<DeliveryHubRoute> list = new ArrayList<>();

        UUID firstDeliver = deliveryDriverClientResponse.getDeliveryDriverId();
        for (int i = 0; i < sortedRoutes.size() - 1; i++) {
            HubRoute current = sortedRoutes.get(i);
            HubRoute next = sortedRoutes.get(i + 1);
            DeliveryHubRoute hubRoute = DeliveryHubRoute.to(current,next.getHubId(),delivery);

            if (i == 0){
                hubRoute.setShipperId(firstDeliver);
                hubRoute.changeStatus(DeliveryStatusEnum.WAITING);
            }

            list.add(deliveryHubRouteRepository.save(hubRoute));
        }

        return list;

    }


    @Transactional
    public void updateDelivery(DeliveryUpdateCommand command) {

        Delivery delivery = deliveryRepository.findById(command.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));

        delivery.setStatus(command.getStatus());

        Integer seq = null;

        if(command.getStatus().equals(DeliveryStatusEnum.ACCEPTED)){

            if(command.getArrivedHub().equals(delivery.getEndHubId())){
                delivery.setStatus(DeliveryStatusEnum.IN_DELIVER);
                IdentityIntegrationDTO identityIntegrationDTO = IdentityIntegrationDTO.builder()
                        .orderId(delivery.getOrderId())
                        .userId(delivery.getCompanyDeliver())
                        .build();
                updateKafkaTemplate.send("integrated-user-topic",identityIntegrationDTO);
                return;
            }

            for(DeliveryHubRoute hubRoute :  delivery.getDeliveryHubRouteList()){
                if(hubRoute.getDestinationHub().equals(command.getArrivedHub())){
                    hubRoute.updateDeliveryStatus(DeliveryStatusEnum.ACCEPTED);
                    seq = hubRoute.getDeliverySequence();
                }
            }

            for(DeliveryHubRoute hubRoute :  delivery.getDeliveryHubRouteList()){
                // 다음 담당자 배정
                if(hubRoute.getDeliverySequence().equals(seq + 1)) {
                    hubRoute.setShipperId(getDeliveryDriver());
                    IdentityIntegrationDTO identityIntegrationDTO = IdentityIntegrationDTO.builder()
                            .orderId(delivery.getOrderId())
                            .userId(hubRoute.getShipperId())
                            .build();
                    updateKafkaTemplate.send("integrated-user-topic",identityIntegrationDTO);
                }
            }
        }
    }

    private UUID getDeliveryDriver() {
        return userInfoClient.getRoute().getDeliveryDriverId();
    }


    @Transactional
    public void deleteDelivery(DeliveryDeleteCommand command) {

        Delivery delivery = deliveryRepository.findById(command.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 배송 정보를 찾을 수 없습니다."));

        delivery.delete(command.getUserName());
    }

    public UUID getOrderIdToHubId(UUID hubId, UUID orderId) {


        Optional<UUID> deliveryHubRoute = deliveryHubRouteRepository.findByOrderIdAndStartHubOrDestinationHub(orderId, hubId);

        if (deliveryHubRoute.isPresent()) {
            return deliveryHubRoute.get();
        } else {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }
    }

    public UUID getOrderIdToDeliver(UUID orderId, UUID userId) {
        Optional<UUID> orderIdList = deliveryHubRouteRepository.findByOrderIdAndSearchDeliver(orderId, userId);

        if (orderIdList.isPresent()) {
            return orderIdList.get();
        } else {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }
    }

    public List<UUID> getOrderIdListToDeliver(UUID userId) {
        Optional<List<UUID>> orderIdList = deliveryHubRouteRepository.findByUserIdSearchDeliver(userId);
        if (orderIdList.isPresent()) {
            return orderIdList.get();
        } else {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }
    }

    public List<UUID> getOrderIdListToHubId(UUID hubId) {
        Optional<List<UUID>> orderIdList = deliveryHubRouteRepository.findByHubIdSearchDeliver(hubId);
        if (orderIdList.isPresent()) {
            return orderIdList.get();
        } else {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }
    }

    public void updateDeliveryEvent(OrderStatusEvent value) {
        Delivery delivery = deliveryRepository.findByOrderId(value.getOrderId());
        delivery.setStatus(DeliveryStatusEnum.valueOf(value.getStatus()));
    }
}
