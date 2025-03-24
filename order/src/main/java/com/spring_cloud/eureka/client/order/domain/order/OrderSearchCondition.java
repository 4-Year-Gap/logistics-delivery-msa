package com.spring_cloud.eureka.client.order.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


@AllArgsConstructor
@Getter
public class OrderSearchCondition {

    private UUID userId;
    private String userRole;

}
