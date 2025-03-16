package com.spring_cloud.eureka.client.order.infrastructure.client.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class UserInfoClientResponse {
    private UUID userId;
    private String userName;
    private String slackId;
}
