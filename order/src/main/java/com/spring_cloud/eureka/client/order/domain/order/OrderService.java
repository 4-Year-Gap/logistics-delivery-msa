package com.spring_cloud.eureka.client.order.domain.order;



import com.spring_cloud.eureka.client.order.infrastructure.client.ProductClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.*;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.OrderCreateEvent;
import com.spring_cloud.eureka.client.order.infrastructure.repository.OrderRepository;

import com.spring_cloud.eureka.client.order.interfaces.OrderReadCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final KafkaTemplate<String,OrderCreateEvent> createKafkaTemplate;
    private final KafkaTemplate<String,IdentityIntegrationDTO> updateKafkaTemplate;
//
    @Value("${kafka.event.name.order-create}")
    private String ORDER_CREATE_TOPIC;

    @Value("${kafka.event.name.product_decrease}")
    private String PRODUCT_DECREASE_TOPIC;

    private String KEY_PREFIX = "ORDER_ID : ";

    @Transactional
    public OrderEntity createOrder(OrderCreateCommand command) {


        ProductClientRequest productClientRequest = ProductClientRequest.create(command);
        ProductClientResponse productClientResponse = productClient.getProduct(productClientRequest);

        //나중에 삭제
        UserInfoClientResponse userInfoClientResponse = new UserInfoClientResponse();
        userInfoClientResponse.setUserId(UUID.randomUUID());
        userInfoClientResponse.setSlackId("test_user_slackId");
        userInfoClientResponse.setUserName("testUser");

        OrderEntity orderEntity = orderRepository.save(orderCreate(userInfoClientResponse,command));


        OrderCreateEvent orderCreateEvent = createOrderEvent(orderEntity,command,productClientResponse);

        IdentityIntegrationDTO identityIntegrationDTO = IdentityIntegrationDTO.builder()
                .orderId(orderEntity.getOrderId())
                .userId(command.getUserId())
                .build();

        createKafkaTemplate.send(ORDER_CREATE_TOPIC,KEY_PREFIX + orderCreateEvent.getOrderId(),orderCreateEvent);
        createKafkaTemplate.send(PRODUCT_DECREASE_TOPIC,orderCreateEvent);
        updateKafkaTemplate.send("integrated-user-topic",identityIntegrationDTO);

        return orderEntity;
    }

    private OrderCreateEvent createOrderEvent(OrderEntity orderEntity, OrderCreateCommand command, ProductClientResponse productClientResponse) {

        return OrderCreateEvent.create(
                orderEntity.getOrderId(),
                productClientResponse.getStartHub(),
                productClientResponse.getEndHub(),
                productClientResponse.getProductId(),
                command.getProductQuantity(),
                command.getReceiverSlackId(),
                command.getAddress(),
                orderEntity.getOrderedBy(),
                command.getUserId(),
                orderEntity.getCompanyDeliver()
        );
    }


    private OrderEntity orderCreate(UserInfoClientResponse userInfoClientResponse, OrderCreateCommand command) {
        return OrderEntity.create(
                userInfoClientResponse.getUserId(),
                command.getProductId(),
                command.getProductPrice(),
                command.getSupplierId(),
                command.getReceivingCompanyId(),
                command.getProductQuantity(),
                command.getRequestMessage(),
                userInfoClientResponse.getUserId()// 일단 임시로 생성 나중에 업체 배송 로직 완료 시 배정 후 여기에 저장
        );
    }

    @Transactional
    public OrderUpdateInfo updateOrder(OrderUpdateCommand command)  {

        OrderEntity orderEntity = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 주문을 찾을 수 없습니다."));

        String userName = "testUser";

        if (!userName.equals(orderEntity.getOrderedBy())){
            throw new IllegalArgumentException("not own order");
        }

        orderEntity.setStatus(command.getOrderEntityStatus());
        return new OrderUpdateInfo(orderEntity);
    }

    public OrderEntity getOneOrderInformationById(OrderReadCommand command) {


      if(command.getUserRole().equals("MASTER")){
          return getOrder(command.getOrderId());
      } else if (command.getUserRole().equals("HUB_MANAGER")) {

      } else if (command.getUserRole().equals("DELIVERY_MANAGER")) {


      } else if (command.getUserRole().equals("COMPANY_MANAGER")) {


      }else{
          throw new IllegalArgumentException("관한이 올바르지 않음");
      }

      return null;
    }


    public OrderEntity getOrder(UUID orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 주문을 찾을 수 없습니다."));
    }
}
