package com.springcloud.client.user.interfaces;

import com.springcloud.client.user.domain.DeliveryCommand;
import com.springcloud.client.user.domain.DeliveryDriverRole;
import com.springcloud.client.user.domain.DeliveryInfo;
import lombok.Getter;

import java.util.UUID;

public class DeliveryDriverDto {

    @Getter
    public static class DeliveryDriverRequest {
        private UUID userId;

        public DeliveryCommand toCommand() {
            return DeliveryCommand.builder()
                    .userId(this.userId)
                    .build();
        }
    }

    @Getter
    public static class DeliveryDriverResponse {
        private final UUID deliveryDriverId;
        private final String username;
        private final String slackId;
        private final DeliveryDriverRole role;

        public DeliveryDriverResponse(DeliveryInfo info) {
            this.deliveryDriverId = info.getDeliveryDriverId();
            this.username = info.getUsername();
            this.slackId = info.getSlackId();
            this.role = info.getRole();
        }
    }
}