package com.springcloud.management.application.dto;

import com.springcloud.management.interfaces.dto.CreateSlackRequest;

import java.util.List;

public record CreateSlackCommand(Integer orderNumber,
                                 String companyName,
                                 String productInfo,
                                 String orderRequest,
                                 String fromHubName,
                                 List<String> stopoverHubNames,
                                 String toHubName,
                                 List<String> deliveryUsers,
                                 String companyDeliveryUserName) {

    public static CreateSlackCommand fromCreateSlackRequest(CreateSlackRequest createSlackRequest){
        return new CreateSlackCommand(
                createSlackRequest.orderNumber(),
                createSlackRequest.companyName(),
                createSlackRequest.productInfo(),
                createSlackRequest.orderRequest(),
                createSlackRequest.fromHubName(),
                createSlackRequest.stopoverHubNames(),
                createSlackRequest.toHubName(),
                createSlackRequest.deliveryUsers(),
                createSlackRequest.companyDeliveryUserName()
        );
    }
}
