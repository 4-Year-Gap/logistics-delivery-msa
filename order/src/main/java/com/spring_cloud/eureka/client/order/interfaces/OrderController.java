package com.spring_cloud.eureka.client.order.interfaces;


import com.spring_cloud.eureka.client.order.application.*;
import com.spring_cloud.eureka.client.order.common.ApiResponse;
import com.spring_cloud.eureka.client.order.domain.order.*;
import com.spring_cloud.eureka.client.order.infrastructure.client.ProductClient;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.IdentityIntegrationResponse;
import com.spring_cloud.eureka.client.order.infrastructure.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    private final KafkaTemplate<String, IdentityIntegrationCommand> updateKafkaTemplate;
    private final RedisTemplate<String, IdentityIntegrationResponse> redisTemplate;
    private final ProductClient client;
    private final OrderRepository orderRepository;


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
            @RequestHeader(name = "X-USER-ROLE") String userRole,
            @RequestBody OrderUpdateRequest orderUpdateRequest) {

        System.out.println(userId + "$$$$$$$$$$$$$$");
        OrderUpdateCommand command = orderUpdateRequest.toCommand(userId,userRole);
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

    @GetMapping("/search")
    public ApiResponse<?> getOrders(
            @RequestHeader(name = "X-USER-ID") UUID userId,
            @RequestHeader(name = "X-USER-ROLE") String userRole,
            Pageable pageable
    ){
        OrderSearchCondition orderSearchCondition = new OrderSearchCondition(userId,userRole);

        return ApiResponse.ok(orderFacade.getOrders(pageable,orderSearchCondition));
    }

    @GetMapping("/test22")
    public OrderEntity test() {


        HashOperations<String, String,IdentityIntegrationResponse> hashOps = redisTemplate.opsForHash();

        IdentityIntegrationResponse value = hashOps.get("identityIntegrationCache","77daaade-593b-4284-8416-b82570e1ce4f");

        return orderRepository.findByOrderIdAndConsumeCompanyIdOrSupplyCompanyId(
                value.getUserId(),
                value.getCompanyId(),
                value.getCompanyId()
        );


    }


    @GetMapping("/test")
    public OrderEntity getStock(@RequestParam UUID userId) {
        UUID companyId = client.getCompanyId(userId);

        return orderRepository.findByOrderIdAndConsumeCompanyIdOrSupplyCompanyId(
                userId,
                companyId,
                companyId
        );
    }




}
