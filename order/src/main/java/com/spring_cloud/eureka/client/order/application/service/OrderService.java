package com.spring_cloud.eureka.client.order.application.service;



import com.spring_cloud.eureka.client.order.application.OrderSearchCondition;
import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import com.spring_cloud.eureka.client.order.domain.order.OrderEntityStatus;
import com.spring_cloud.eureka.client.order.infrastructure.client.HubClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.ProductClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.UserInfoClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.*;
import com.spring_cloud.eureka.client.order.infrastructure.repository.OrderRepository;

import com.spring_cloud.eureka.client.order.presentation.dto.request.OrderCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final HubClient hubClient;
    private final UserInfoClient userInfoClient;

    @Transactional
    public OrderEntity createOrder(OrderCreateRequest orderCreateRequest, Integer userId) {


        ProductClientRequest productClientRequest = ProductClientRequest.create(orderCreateRequest);
//        ProductClientResponse productClientResponse = productClient.getProduct(orderCreateRequest.getProductId(), productClientRequest).data();
       //니중에 삭제
        ProductClientResponse productClientResponse = new ProductClientResponse();
        productClientResponse.setEndHub(UUID.randomUUID());
        productClientResponse.setStartHub(UUID.randomUUID());
        productClientResponse.setProductId(UUID.randomUUID());
        productClientResponse.setStock(100);
        // 나중에 주석 해제
//        UserInfoClientResponse userInfoClientResponse = userInfoClient.getUserInfo(userId).data();

        //나중에 삭제
        UserInfoClientResponse userInfoClientResponse = new UserInfoClientResponse();
        userInfoClientResponse.setUserId(UUID.randomUUID());
        userInfoClientResponse.setSlackId("test_user_slackId");
        userInfoClientResponse.setUserName("testUser");

        OrderEntity orderEntity = orderRepository.save(orderCreate(userInfoClientResponse,orderCreateRequest));

        //배송으로 createdOrderEvent

        return orderEntity;
    }

    private OrderEntity orderCreate(UserInfoClientResponse userInfoClientResponse, OrderCreateRequest orderCreateRequest) {
        return OrderEntity.create(
                userInfoClientResponse.getUserName(),
                orderCreateRequest.getProductId(),
                orderCreateRequest.getProductPrice(),
                orderCreateRequest.getSupplierId(),
                orderCreateRequest.getReceivingCompanyId(),
                orderCreateRequest.getProductQuantity(),
                orderCreateRequest.getRequestMessage()
        );
    }

    @Transactional
    public void updateOrder(UUID updateOrderId, OrderEntityStatus orderEntityStatus, Integer userId)  {

        OrderEntity orderEntity = orderRepository.findById(updateOrderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 주문을 찾을 수 없습니다."));

//        UserInfoClientResponse userInfoClientResponse = userInfoClient.getUserInfo(userId).data();

        String userName = "testUser";

        if (!userName.equals(orderEntity.getOrderedBy())){
            throw new IllegalArgumentException("not own order");
        }

        orderEntity.setStatus(orderEntityStatus);
    }

    public OrderEntity getOneOrderInformationById(UUID orderId) {

      OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 주문을 찾을 수 없습니다."));

      return orderEntity;
    }

    public Page<OrderEntity> getOrders(Integer userId, String userRole, Pageable pageable) {


        OrderSearchCondition searchCondition = new OrderSearchCondition(userId,userRole,pageable);


        return  orderRepository.search(searchCondition);
    }


}
