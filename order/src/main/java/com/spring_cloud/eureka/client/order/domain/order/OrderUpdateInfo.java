package com.spring_cloud.eureka.client.order.domain.order;


import java.util.UUID;

public class OrderUpdateInfo {

    private UUID orderedBy;
    private UUID consumeCompanyId;
    private UUID supplyCompanyId;
    private UUID productId;
    private Integer quantity;
    private Integer totalPrice;
    private String requestMessage;
    private OrderEntityStatus status;

    public OrderUpdateInfo(OrderEntity orderEntity) {
        this.orderedBy = orderEntity.getOrderedBy();
        this.consumeCompanyId = orderEntity.getConsumeCompanyId();
        this.supplyCompanyId = orderEntity.getSupplyCompanyId();
        this.productId = orderEntity.getProductId();
        this.quantity = orderEntity.getQuantity();
        this.totalPrice = orderEntity.getTotalPrice();
        this.requestMessage = orderEntity.getRequestMessage();
        this.status = orderEntity.getStatus();
    }
}
