package com.spring_cloud.eureka.client.order.infrastructure.client.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductClientResponse {
    @Override
    public String toString() {
        return "ProductClientResponse{" +
                "productId=" + productId +
                ", startHub=" + startHub +
                ", endHub=" + endHub +
                '}';
    }

    private UUID productId; //제품 아이디
    private UUID startHub; // 제품 배달에 대한 시작 허브
    private UUID endHub; // 제품 배달에 대한 마지막 허브


}
