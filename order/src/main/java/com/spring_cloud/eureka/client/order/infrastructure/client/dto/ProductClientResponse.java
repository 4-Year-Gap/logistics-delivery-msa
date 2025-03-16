package com.spring_cloud.eureka.client.order.infrastructure.client.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductClientResponse {
    private UUID productId; //제품 아이디
    private Integer stock;// 수량 이거는 필요 없을 거 같긴한데 일단 명세서에 있어서 적었습니다
    private UUID startHub; // 제품 배달에 대한 시작 허브
    private UUID endHub; // 제품 배달에 대한 마지막 허브

    @Override
    public String toString() {
        return "ProductClientResponse{" +
                "productId=" + productId +
                ", stock=" + stock +
                '}';
    }
}
