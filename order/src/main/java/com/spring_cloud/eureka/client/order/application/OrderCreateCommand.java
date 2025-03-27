package com.spring_cloud.eureka.client.order.application;


import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class OrderCreateCommand {

    private UUID supplierId;// 제품 공급 업체
    private UUID receivingCompanyId;// 제품 수요 업체
    private UUID productId; // 주문한 제품
    private String address; // 배송지
    private Integer productQuantity; // 배송 수량
    private Integer productPrice; // 총합 가격
    private String requestMessage;
    private UUID userId;
    private String receiverSlackId;

}
