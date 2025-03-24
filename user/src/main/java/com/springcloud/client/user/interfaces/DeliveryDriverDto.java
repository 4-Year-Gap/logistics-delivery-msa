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
        private UUID hubId;
        private UUID deliveryDriverId;
        private String username;
        private String slackId;
        private DeliveryDriverRole role;

        public DeliveryCommand toCommand() {
            return DeliveryCommand.builder()
                    .userId(this.userId)
                    .hubId(this.hubId)
                    .build();
        }

        public DeliveryCommand toCommand(UUID deliveryDriverId) {
            return DeliveryCommand.builder()
                    .userId(this.userId)
                    .hubId(this.hubId)
                    .deliveryDriverId(deliveryDriverId)
                    .username(this.username)
                    .slackId(this.slackId)
                    .role(this.role)
                    .build();
        }
    }

    @Getter
    public static class DeliveryDriverResponse {
        private final UUID userId;
        private final UUID deliveryDriverId;
        private final String username;
        private final String slackId;
        private final DeliveryDriverRole role;
        private final UUID hubId;

        public DeliveryDriverResponse(DeliveryInfo info) {
            this.userId = info.getUserId();
            this.deliveryDriverId = info.getDeliveryDriverId();
            this.username = info.getUsername();
            this.slackId = info.getSlackId();
            this.role = info.getRole();
            this.hubId = info.getHubId();
        }
    }
}