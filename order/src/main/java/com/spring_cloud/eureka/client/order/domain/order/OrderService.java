package com.spring_cloud.eureka.client.order.domain.order;


import com.spring_cloud.eureka.client.order.infrastructure.client.DeliveryClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.ProductClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.*;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.OrderCreateEvent;
import com.spring_cloud.eureka.client.order.infrastructure.repository.OrderRepository;

import com.spring_cloud.eureka.client.order.interfaces.OrderReadCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final DeliveryClient deliveryClient;
    private final KafkaTemplate<String, OrderCreateEvent> createKafkaTemplate;
    private final KafkaTemplate<String, IdentityIntegrationCommand> updateKafkaTemplate;
    private final RedisTemplate<String, IdentityIntegrationResponse> redisTemplate;

    @Value("${kafka.event.name.order-create}")
    private String ORDER_CREATE_TOPIC;

    @Value("${kafka.event.name.product_decrease}")
    private String PRODUCT_DECREASE_TOPIC;

    @Value("${kafka.event.name.integrated-user-topic}")
    private String INTEGRATED_USER_TOPIC;

    private final String CREATE_KEY_PREFIX = "ORDER_ID:";

    private final String INTEGRATED_KEY = "ORDER:CREATE";

    private final String REDIS_INTEGRATED_KEY = "identityIntegrationCache";
    @Transactional
    public OrderEntity createOrder(OrderCreateCommand command) {


        ProductClientRequest productClientRequest = ProductClientRequest.create(command);
//        ProductClientResponse productClientResponse = productClient.getProduct(productClientRequest);


        ProductClientResponse productClientResponse = new ProductClientResponse();
        productClientResponse.setEndHub(UUID.fromString("86c9cc97-0270-11f0-87a5-0242ac130003"));
        productClientResponse.setStartHub(UUID.fromString("86d4917f-0270-11f0-87a5-0242ac130003"));
        productClientResponse.setProductId(command.getProductId());


        //나중에 삭제
        UserInfoClientResponse userInfoClientResponse = new UserInfoClientResponse();
        userInfoClientResponse.setUserId(UUID.randomUUID());
        userInfoClientResponse.setSlackId("test_user_slackId");
        userInfoClientResponse.setUserName("testUser");

        OrderEntity orderEntity = orderRepository.save(orderCreate(userInfoClientResponse, command));


        OrderCreateEvent orderCreateEvent = createOrderEvent(orderEntity, command, productClientResponse);

        IdentityIntegrationCommand identityIntegrationDTO = IdentityIntegrationCommand.builder()
                .orderId(orderEntity.getOrderId())
                .userId(command.getUserId())
                .build();

        createKafkaTemplate.send(ORDER_CREATE_TOPIC, CREATE_KEY_PREFIX + orderCreateEvent.getOrderId(), orderCreateEvent);
//        createKafkaTemplate.send(PRODUCT_DECREASE_TOPIC, orderCreateEvent);
//        updateKafkaTemplate.send(INTEGRATED_USER_TOPIC,INTEGRATED_KEY, identityIntegrationDTO);

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
    public OrderUpdateInfo updateOrder(OrderUpdateCommand command) {

        OrderEntity orderEntity = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 주문을 찾을 수 없습니다."));


        if (!command.getUserId().equals(orderEntity.getOrderedBy())) {
            throw new IllegalArgumentException("not own order");
        }

        orderEntity.setStatus(command.getOrderEntityStatus());
        return new OrderUpdateInfo(orderEntity);
    }

    public OrderEntity getOneOrderInformationById(OrderReadCommand command) {
        return switch (UserRole.valueOf(command.getUserRole())) {
            case MASTER -> orderRepository.findByOrderId(command.getOrderId());
            case COMPANY_MANAGER -> getOrderForCompanyManager(command);
            case DELIVERY_STAFF -> getOrderForDeliveryStaff(command);
            case HUB_MANAGER -> getOrderForHubManager(command);
            default -> throw new IllegalArgumentException("접근할 수 없는 주문");
        };
    }

    private OrderEntity getOrderForCompanyManager(OrderReadCommand command) {
        IdentityIntegrationResponse integrationResponse = getIdentityIntegrationCache(command.getUserId());
        return orderRepository.findByOrderIdAndConsumeCompanyIdOrSupplyCompanyId(
                command.getOrderId(),
                integrationResponse.getCompanyId(),
                integrationResponse.getCompanyId()
        );
    }

    private OrderEntity getOrderForDeliveryStaff(OrderReadCommand command) {
        UUID orderId = deliveryClient.getOrderIdToDeliver(command.getOrderId(), command.getUserId());
        if (orderId == null) {
            throw new OrderNotFoundException("주문이 존재하지 않습니다");
        }
        return orderRepository.findByOrderIdAndDeletedAtIsNull(orderId);
    }

    private OrderEntity getOrderForHubManager(OrderReadCommand command) {
        IdentityIntegrationResponse integrationResponse = getIdentityIntegrationCache(command.getUserId());
        UUID orderId = deliveryClient.getOrderIdToHubId(integrationResponse.getHubId(), command.getOrderId());
        return Optional.ofNullable(orderId)
                .flatMap(orderRepository::findById)
                .orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다"));
    }

    private IdentityIntegrationResponse getIdentityIntegrationCache(UUID userId) {
        HashOperations<String, String, IdentityIntegrationResponse> hashOps = redisTemplate.opsForHash();

        return hashOps.get("identityIntegrationCache", userId.toString());
    }

    // 사용자 정의 예외 클래스
    static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) {
            super(message);
        }
    }


//    public List<OrderEntity> searchOrders(Pageable pageable, OrderSearchCondition orderSearchCondition) {
//
//
//        ListOperations<String, Object> listOps = redisTemplate.opsForList();
//        List<UUID> list = listOps.range(key, 0, -1);
//
//        return orderRepository.findAllById(list,pageable,orderSearchCondition)
//                .orElseThrow(() -> new IllegalArgumentException("주문 ID에 해당하는 주문을 찾을 수 없습니다."));
//    }


}
