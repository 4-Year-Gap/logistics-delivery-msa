package com.springcloud.client.delivery.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HubClientResponse<T> {

    private Integer code;
    private String status;
    private String message;
    private T data;

}
