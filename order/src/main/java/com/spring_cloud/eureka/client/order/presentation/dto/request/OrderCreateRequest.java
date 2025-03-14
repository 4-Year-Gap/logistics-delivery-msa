package com.spring_cloud.eureka.client.order.presentation.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest{

    private UUID supplierId;// 제품 공급 업체
    private UUID receivingCompanyId;// 제품 수요 업체
    private UUID productId; // 주문한 제품
    private String address; // 배송지
    private Integer productQuantity; // 배송 수량
    private Integer productPrice; // 총합 가격
    private String requestMessage;

}
