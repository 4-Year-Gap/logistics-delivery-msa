package com.springcloud.management.infrastructure.external;

public interface SlackClientAdapter {
    void sendSlackMessageToDeliveryChannel(String message);
}
