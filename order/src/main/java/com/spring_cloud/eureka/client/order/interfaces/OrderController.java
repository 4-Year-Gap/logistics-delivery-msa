package com.spring_cloud.eureka.client.order.interfaces;


import com.spring_cloud.eureka.client.order.application.OrderFacade;
import com.spring_cloud.eureka.client.order.common.ApiResponse;
import com.spring_cloud.eureka.client.order.domain.order.OrderCreateCommand;
import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;
import com.spring_cloud.eureka.client.order.domain.order.OrderUpdateCommand;
import com.spring_cloud.eureka.client.order.domain.order.OrderUpdateInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderFacade orderFacade;


    @PostMapping
    public ApiResponse<?> createOrder(
            @RequestBody OrderCreateRequest orderCreateRequest,
            @Header(name = "X-USER-ID") UUID userId
            ) {


        OrderCreateCommand command = orderCreateRequest.toCommand(userId);
        OrderEntity orderInfo = orderFacade.createOrder(command);
        //info response 바꾸고 리턴

        return ApiResponse.created(orderInfo);
    }


    @PatchMapping
    public ApiResponse<?> updateOrder(
            @Header(name = "X-USER-ID") Integer userId,
            @RequestBody OrderUpdateRequest orderUpdateRequest) {

        OrderUpdateCommand command = orderUpdateRequest.toCommand(userId);
        OrderUpdateInfo info = orderFacade.updateOrder(command);
        return ApiResponse.ok("일단 업데이트 성공");
    }


    @GetMapping("/{orderId}")
    public ApiResponse<?> getOneOrderInformationById(
            @PathVariable(name = "orderId") UUID orderId,
            @Header("X-USER-ID") UUID userId,
            @Header("X-USER-ROLE") String userRole
                                                     ){
        OrderReadCommand command = new OrderReadCommand(orderId,userId,userRole);
        return ApiResponse.ok(orderFacade.getOneOrderInformationById(command));
    }

    @GetMapping("/search")
    public ApiResponse<?> getOrders(
            @Header(name = "X-USER-ID") Integer userId,
            @Header(name = "X-USER-ROLE") String userRole,
            Pageable pageable
    ){
//        return ApiResponse.ok(orderService.getOrders(userId,userRole,pageable));
        return null;
    }

}
