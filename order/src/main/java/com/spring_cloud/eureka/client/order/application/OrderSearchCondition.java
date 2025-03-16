package com.spring_cloud.eureka.client.order.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;


@AllArgsConstructor
@Getter
public class OrderSearchCondition {

    private Integer userId;
    private String userRole;

    private Pageable pageable;

}
