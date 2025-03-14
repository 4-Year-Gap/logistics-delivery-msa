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
        ProductClientResponse productClientResponse = productClient.getProduct(orderCreateRequest.getProductId(), productClientRequest).data();

        HubRouteRequest hubRouteRequest = HubRouteRequest.create(productClientResponse);
        HubClientResponse hubClientResponse = hubClient.getRoute(hubRouteRequest).data();

        UserInfoClientResponse userInfoClientResponse = userInfoClient.getUserInfo(userId).data();

        OrderEntity orderEntity = orderRepository.save(
                OrderEntity.create(
                        userInfoClientResponse,
                        orderCreateRequest
                )
        );






        return orderEntity;
    }

    private Delivery createDelivery(OrderCreateRequest orderCreateRequest,
                                    ProductClientResponse productClientResponse,
                                    List<DeliveryHubRoute> shortestHubRoute,
                                    String slackId,
                                    UUID userId
    ) {
        return Delivery.create(
                orderCreateRequest.getAddress(),
                DeliveryStatusEnum.ACCEPTED,
                productClientResponse.getStartHub(),
                productClientResponse.getEndHub(),
                slackId,
                userId,
                shortestHubRoute
        );
    }
}
