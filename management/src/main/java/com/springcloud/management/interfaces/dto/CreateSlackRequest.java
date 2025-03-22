package com.springcloud.management.interfaces.dto;


import java.util.List;

public record CreateSlackRequest(Integer orderNumber,
                                 String companyName,
                                 String productInfo,
                                 String orderRequest,
                                 String fromHubName,
                                 List<String> stopoverHubNames,
                                 String toHubName,
                                 List<String> deliveryUsers,
                                 String companyDeliveryUserName) {
}
