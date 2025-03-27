package com.spring_cloud.eureka.client.order.infrastructure.client.dto;


import com.spring_cloud.eureka.client.order.application.OrderCreateCommand;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductClientRequest {


    private UUID receivingCompanyId;// 제품 수요 업체 이걸 통해서 해당 업체가 소속되어 있는 허브를 찾을 수 있음
    private UUID productId; // 주문한 제품 이걸 통해서 수량 체크 및 시작 허브를 알 수 있음
    private Integer quantity;// 주문한 제품의 수량


    public static ProductClientRequest create(OrderCreateCommand command){
        return ProductClientRequest.builder()
                .receivingCompanyId(command.getReceivingCompanyId())
                .productId(command.getProductId())
                .quantity(command.getProductQuantity())
                .build();
    }
}
