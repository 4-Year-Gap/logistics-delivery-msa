package com.spring_cloud.eureka.client.order.interfaces;


import com.spring_cloud.eureka.client.order.application.OrderCreateCommand;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderCreateRequest{
    private UUID supplierId;// 제품 공급 업체
    private UUID receivingCompanyId;// 제품 수요 업체
    private UUID productId; // 주문한 제품
    private String address; // 배송지
    private Integer productQuantity; // 배송 수량
    private Integer productPrice; // 총합 가격
    private String requestMessage;
    private String receiverSlackId;

    public OrderCreateCommand toCommand(UUID id){
        return OrderCreateCommand.builder()
                .supplierId(supplierId)
                .receivingCompanyId(receivingCompanyId)
                .productId(productId)
                .address(address)
                .productQuantity(productQuantity)
                .productPrice(productPrice)
                .requestMessage(requestMessage)
                .userId(id)
                .receiverSlackId(receiverSlackId)
                .build();
    }
}