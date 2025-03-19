package com.spring_cloud.eureka.client.order.domain.order;



import com.spring_cloud.eureka.client.order.infrastructure.client.ProductClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.*;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.OrderCreateEvent;
import com.spring_cloud.eureka.client.order.infrastructure.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final KafkaTemplate<String,OrderCreateEvent> kafkaTemplate;

    @Transactional
    public OrderEntity createOrder(OrderCreateCommand command) {


        ProductClientRequest productClientRequest = ProductClientRequest.create(command);
//        ProductClientResponse productClientResponse = productClient.getProduct(productClientRequest);
        ProductClientResponse productClientResponse  = new ProductClientResponse();
        productClientResponse.setStartHub(UUID.randomUUID());
        productClientResponse.setProductId(UUID.randomUUID());
        productClientResponse.setEndHub(UUID.randomUUID());

//        UserInfoClientResponse userInfoClientResponse = userInfoClient.getUserInfo(userId).data();

        //나중에 삭제
        UserInfoClientResponse userInfoClientResponse = new UserInfoClientResponse();
        userInfoClientResponse.setUserId(UUID.randomUUID());
        userInfoClientResponse.setSlackId("test_user_slackId");
        userInfoClientResponse.setUserName("testUser");

        OrderEntity orderEntity = orderRepository.save(orderCreate(userInfoClientResponse,command));


        OrderCreateEvent orderCreateEvent = createOrderEvent(orderEntity,command,productClientResponse);



        kafkaTemplate.send("order_topic","asd",orderCreateEvent);
//        kafkaTemplate.send("product_decrease",orderCreateEvent.toJson());

        return orderEntity;
    }

    private OrderCreateEvent createOrderEvent(OrderEntity orderEntity, OrderCreateCommand command, ProductClientResponse productClientResponse) {

        return OrderCreateEvent.create(
                orderEntity.getOrderId(),
                productClientResponse.getStartHub(),
                productClientResponse.getEndHub(),
                productClientResponse.getProductId(),
                command.getProductQuantity()
        );
    }


    private OrderEntity orderCreate(UserInfoClientResponse userInfoClientResponse, OrderCreateCommand command) {
        return OrderEntity.create(
                userInfoClientResponse.getUserName(),
                command.getProductId(),
                command.getProductPrice(),
                command.getSupplierId(),
                command.getReceivingCompanyId(),
                command.getProductQuantity(),
                command.getRequestMessage()
        );
    }

    @Transactional
    public OrderUpdateInfo updateOrder(OrderUpdateCommand command)  {

        OrderEntity orderEntity = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 주문을 찾을 수 없습니다."));

//        UserInfoClientResponse userInfoClientResponse = userInfoClient.getUserInfo(userId).data();

        String userName = "testUser";

        if (!userName.equals(orderEntity.getOrderedBy())){
            throw new IllegalArgumentException("not own order");
        }

        orderEntity.setStatus(command.getOrderEntityStatus());
        return new OrderUpdateInfo(orderEntity);
    }

    public OrderEntity getOneOrderInformationById(UUID orderId) {

      OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 주문을 찾을 수 없습니다."));

      return orderEntity;
    }

//    public Page<OrderEntity> getOrders(Integer userId, String userRole, Pageable pageable) {
//
//
//        OrderSearchCondition searchCondition = new OrderSearchCondition(userId,userRole,pageable);
//
//
//        return  orderRepository.search(searchCondition,pageable);
//
//    }

}
