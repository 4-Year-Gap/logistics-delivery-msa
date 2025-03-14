package com.spring_cloud.eureka.client.order.application.service;


import com.spring_cloud.eureka.client.order.domain.delivery.Delivery;
import com.spring_cloud.eureka.client.order.domain.delivery.DeliveryHubRoute;
import com.spring_cloud.eureka.client.order.domain.delivery.DeliveryStatusEnum;
import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import com.spring_cloud.eureka.client.order.infrastructure.client.HubClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.ProductClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.UserInfoClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.*;
import com.spring_cloud.eureka.client.order.infrastructure.repository.OrderRepository;
import com.spring_cloud.eureka.client.order.presentation.dto.OrderUpdateRequest;
import com.spring_cloud.eureka.client.order.presentation.dto.request.OrderCreateRequest;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final HubClient hubClient;
    private final UserInfoClient userInfoClient;

    @Transactional
    public OrderEntity createOrder(OrderCreateRequest orderCreateRequest, UUID userId) {


        ProductClientRequest productClientRequest = ProductClientRequest.create(orderCreateRequest);
//        ProductClientResponse productClientResponse = productClient.getProduct(orderCreateRequest.getProductId(), productClientRequest).data();
       //니중에 삭제
        ProductClientResponse productClientResponse = new ProductClientResponse();
        productClientResponse.setEndHub(UUID.randomUUID());
        productClientResponse.setStartHub(UUID.randomUUID());
        productClientResponse.setProductId(UUID.randomUUID());
        productClientResponse.setStock(100);

        HubRouteRequest hubRouteRequest = HubRouteRequest.create(productClientResponse);
        // 나중에 주석 해제
//        HubClientResponse hubClientResponse = hubClient.getRoute(hubRouteRequest).data();
//        UserInfoClientResponse userInfoClientResponse = userInfoClient.getUserInfo(userId).data();

        //나중에 삭제
        UserInfoClientResponse userInfoClientResponse = new UserInfoClientResponse();
        userInfoClientResponse.setUserId(UUID.randomUUID());
        userInfoClientResponse.setSlackId("test_user_slackId");
        userInfoClientResponse.setUserName("testUser");

        OrderEntity orderEntity = orderRepository.save(
                OrderEntity.create(
                        userInfoClientResponse,
                        orderCreateRequest
                )
        );



        return orderEntity;
    }

    @Transactional
    public void updateOrder(OrderUpdateRequest orderUpdateRequest,UUID userId) throws IllegalAccessException {

        OrderEntity orderEntity = orderRepository.findById(orderUpdateRequest.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 주문을 찾을 수 없습니다."));

//        UserInfoClientResponse userInfoClientResponse = userInfoClient.getUserInfo(userId).data();

        String userName = "testUser";

        if (!userName.equals(orderEntity.getOrderedBy())){
            throw new IllegalAccessException("not own order");
        }

        orderEntity.setStatus(orderUpdateRequest.getOrderEntityStatus());
    }

}
