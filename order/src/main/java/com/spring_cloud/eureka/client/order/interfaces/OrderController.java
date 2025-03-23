package com.spring_cloud.eureka.client.order.interfaces;


import com.spring_cloud.eureka.client.order.application.OrderFacade;
import com.spring_cloud.eureka.client.order.common.ApiResponse;
import com.spring_cloud.eureka.client.order.domain.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    private final KafkaTemplate<String, IdentityIntegrationCommand> updateKafkaTemplate;
    private final RedisTemplate<String, IdentityIntegrationCommand> redisTemplate;


    @PostMapping
    public ApiResponse<?> createOrder(
            @RequestBody OrderCreateRequest orderCreateRequest,
            @RequestHeader(name = "X-USER-ID") UUID userId
    ) {


        OrderCreateCommand command = orderCreateRequest.toCommand(userId);
        OrderEntity orderInfo = orderFacade.createOrder(command);
        //info response 바꾸고 리턴

        return ApiResponse.created(orderInfo);
    }


    @PatchMapping
    public ApiResponse<?> updateOrder(
            @RequestHeader(name = "X-USER-ID") UUID userId,
            @RequestBody OrderUpdateRequest orderUpdateRequest) {

        System.out.println(userId + "$$$$$$$$$$$$$$");
        OrderUpdateCommand command = orderUpdateRequest.toCommand(userId);
        OrderUpdateInfo info = orderFacade.updateOrder(command);
        return ApiResponse.ok("일단 업데이트 성공");
    }


    @GetMapping("/{orderId}")
    public ApiResponse<?> getOneOrderInformationById(
            @PathVariable(name = "orderId") UUID orderId,
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestHeader("X-USER-ROLE") String userRole
    ) {
        OrderReadCommand command = new OrderReadCommand(orderId, userId, userRole);
        return ApiResponse.ok(orderFacade.getOneOrderInformationById(command));
    }
//
//    @GetMapping("/search")
//    public ApiResponse<?> getOrders(
//            @Header(name = "X-USER-ID") Integer userId,
//            @Header(name = "X-USER-ROLE") String userRole,
//            Pageable pageable
//    ){
//        OrderSearchCondition orderSearchCondition = new OrderSearchCondition(userId,userRole);// 나중에 condition
//
//        return ApiResponse.ok(orderFacade.getOrders(pageable,orderSearchCondition));
//    }

    @GetMapping("/test")
    public void test() {


        HashOperations<String, String,IdentityIntegrationCommand> hashOps = redisTemplate.opsForHash();

        IdentityIntegrationCommand value = hashOps.get("identityIntegrationCache","18d703d7-8299-451f-9678-d5684bb48348");

        System.out.println(value.getOrderId());


    }


    @GetMapping("/test2")
    public void tesst() {

        IdentityIntegrationCommand identityIntegrationCommand = IdentityIntegrationCommand.builder().
                userId(UUID.fromString("a8517dce-9239-4695-a76c-210136566210"))
                .orderId(UUID.randomUUID()).
                build();


        updateKafkaTemplate.send("integrated-user-topic","ORDER:CREATE", identityIntegrationCommand);

    }

}
