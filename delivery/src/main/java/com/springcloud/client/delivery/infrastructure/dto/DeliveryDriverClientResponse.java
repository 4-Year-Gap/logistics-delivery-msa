package com.springcloud.client.delivery.infrastructure.dto;

import com.springcloud.client.delivery.domain.delivery.DeliveryDriverRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDriverClientResponse {
    private UUID deliveryDriverId;
    private String username;
    private String slackId;
    private DeliveryDriverRole role;

}
