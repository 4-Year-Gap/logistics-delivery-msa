package com.spring_cloud.eureka.client.order.domain.order;


import com.spring_cloud.eureka.client.order.application.IdentityIntegrationCommand;
import com.spring_cloud.eureka.client.order.application.OrderCreateCommand;
import com.spring_cloud.eureka.client.order.application.OrderUpdateCommand;
import com.spring_cloud.eureka.client.order.infrastructure.client.DeliveryClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.ProductClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.*;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.OrderCreateEvent;
import com.spring_cloud.eureka.client.order.infrastructure.repository.OrderRepository;

import com.spring_cloud.eureka.client.order.application.OrderReadCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
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
    private final KafkaTemplate<String, OrderCreateEvent> productUpdateEventKafkaTemplate;
    private final KafkaTemplate<String, IdentityIntegrationCommand> userUpdateKafkaTemplate;
    private final KafkaTemplate<String, OrderStatusEvent> orderStausEventKafkaTemplate;
    private final RedisTemplate<String, IdentityIntegrationResponse> redisTemplate;

    @Value("${kafka.event.name.order-create}")
    private String ORDER_CREATE_TOPIC;

    @Value("${kafka.event.name.product_decrease}")
    private String PRODUCT_DECREASE_TOPIC;

    @Value("${kafka.event.name.integrated-user-topic}")
    private String INTEGRATED_USER_TOPIC;

    @Value("${kafka.event.name.order-status}")
    private String ORDER_STATUS_TOPIC;

    private final String CREATE_KEY_PREFIX = "ORDER_ID:";

    private final String INTEGRATED_KEY = "ORDER:CREATE";

    private final String REDIS_INTEGRATED_KEY = "identityIntegrationCache";
    @Transactional
    public OrderEntity createOrder(OrderCreateCommand command) {

        ProductClientRequest productClientRequest = ProductClientRequest.create(command);
        ProductClientResponse productClientResponse = productClient.getProduct(productClientRequest);




        UserInfoClientResponse userInfoClientResponse = new UserInfoClientResponse();


        OrderEntity orderEntity = orderRepository.save(orderCreate(userInfoClientResponse, command));


        OrderCreateEvent orderCreateEvent = createOrderEvent(orderEntity, command, productClientResponse);

        ProductUpdateEvent productUpdateEvent = createProductEvent(productClientResponse.getProductId(),command.getProductQuantity());
        
        IdentityIntegrationCommand identityIntegrationDTO = IdentityIntegrationCommand.builder()
                .orderId(orderEntity.getOrderId())
                .userId(command.getUserId())
                .build();





        createKafkaTemplate.send(ORDER_CREATE_TOPIC, CREATE_KEY_PREFIX + orderCreateEvent.getOrderId(), orderCreateEvent);
//        productUpdateEventKafkaTemplate.send(PRODUCT_DECREASE_TOPIC, orderCreateEvent);






        userUpdateKafkaTemplate.send(INTEGRATED_USER_TOPIC,INTEGRATED_KEY, identityIntegrationDTO);





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


        if (!udpateRoleCheck(command.getUserRole())){
            throw new IllegalArgumentException("권한이 없습니다");
        }

        OrderReadCommand readCommand = new OrderReadCommand(command.getOrderId(),command.getUserId(),command.getUserRole());
        OrderEntity orderEntity = getOneOrderInformationById(readCommand);

//
//        if (command.getOrderEntityStatus().equals(OrderEntityStatus.CANCELED)){
//            ProductUpdateEvent productUpdateEvent = createProductEvent(orderEntity.getProductId(),orderEntity.getQuantity() * -1);
//            productUpdateEventKafkaTemplate.send(PRODUCT_DECREASE_TOPIC, productUpdateEvent);
//            OrderStatusEvent orderStatusEvent = createOrderStatusEvent(orderEntity.getOrderId(),command.getOrderEntityStatus());
//            orderStausEventKafkaTemplate.send(ORDER_STATUS_TOPIC,orderStatusEvent);
//        }



        orderEntity.setStatus(command.getOrderEntityStatus());
        return new OrderUpdateInfo(orderEntity);
    }

    private boolean udpateRoleCheck(String userRole) {
        if (userRole == null) {
            return false;
        }
        return userRole.equals(UserRole.MASTER.name()) || userRole.equals(UserRole.HUB_MANAGER.name());
    }

    private OrderStatusEvent createOrderStatusEvent(UUID orderId, OrderEntityStatus orderEntityStatus) {
        return OrderStatusEvent.create(orderId,orderEntityStatus);
    }


    private ProductUpdateEvent createProductEvent(UUID productId, Integer quantity) {
        return ProductUpdateEvent.create(productId,quantity);
    }

    public OrderEntity getOneOrderInformationById(OrderReadCommand command) {
        return switch (UserRole.valueOf(command.getUserRole())) {
            case MASTER -> orderRepository.findByOrderId(command.getOrderId());
            case COMPANY_MANAGER -> getOrderForCompanyManager(command);
            case DELIVERY_STAFF -> getOrderForDeliveryStaff(command);
            case HUB_MANAGER -> getOrderForHubManager(command);
        };
    }

//    private OrderEntity getOrderForCompanyManager(OrderReadCommand command) {
//        IdentityIntegrationResponse integrationResponse = getIdentityIntegrationCache(command.getUserId());
//        return orderRepository.findByOrderIdAndConsumeCompanyIdOrSupplyCompanyId(
//                command.getOrderId(),
//                integrationResponse.getCompanyId(),
//                integrationResponse.getCompanyId()
//        );
//    }

    private OrderEntity getOrderForCompanyManager(OrderReadCommand command) {
        UUID companyId = productClient.getCompanyId(command.getUserId());


        return orderRepository.findByOrderIdAndConsumeCompanyIdOrSupplyCompanyId(
                command.getOrderId(),
                companyId,
                companyId
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

        IdentityIntegrationResponse identityIntegrationCache = hashOps.get("identityIntegrationCache", userId.toString());

        if (null == identityIntegrationCache){
            throw new IllegalArgumentException("레디스에 존재 하지 않음");
        }

        return identityIntegrationCache;
    }

    // 사용자 정의 예외 클래스
    static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) {
            super(message);
        }
    }


    public Page<OrderEntity> searchOrders(Pageable pageable, OrderSearchCondition orderSearchCondition) {

        return switch (UserRole.valueOf(orderSearchCondition.getUserRole())) {
            case MASTER -> orderRepository.findAll(pageable);
            case COMPANY_MANAGER -> getOrderListForCompanyManager(pageable,orderSearchCondition);
            case DELIVERY_STAFF ->getOrderListForDeliveryStaff(pageable,orderSearchCondition);
            case HUB_MANAGER -> getOrderListForHubManager(pageable,orderSearchCondition);
        };
    }

    private Page<OrderEntity> getOrderListForHubManager(Pageable pageable, OrderSearchCondition orderSearchCondition) {
        IdentityIntegrationResponse integrationResponse = getIdentityIntegrationCache(orderSearchCondition.getUserId());
        List<UUID> orderIdList = deliveryClient.getOrderIdListToHub(integrationResponse.getHubId());
        return orderRepository.findAllByOrderIdIn(orderIdList,pageable);
    }

    private Page<OrderEntity> getOrderListForDeliveryStaff(Pageable pageable, OrderSearchCondition orderSearchCondition) {
        List<UUID> orderIdList = deliveryClient.getOrderIdListToDeliver(orderSearchCondition.getUserId());
        return orderRepository.findAllByOrderIdIn(orderIdList,pageable);

    }

    private Page<OrderEntity> getOrderListForCompanyManager(Pageable pageable, OrderSearchCondition orderSearchCondition) {
        IdentityIntegrationResponse integrationResponse = getIdentityIntegrationCache(orderSearchCondition.getUserId());
        return orderRepository.findAllByConsumeCompanyIdOrSupplyCompanyId(integrationResponse.getCompanyId(),integrationResponse.getCompanyId(),pageable);
    }

}
