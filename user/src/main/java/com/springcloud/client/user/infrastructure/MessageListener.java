package com.springcloud.client.user.infrastructure;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface MessageListener {

    void consumeMessage(ConsumerRecord<String, IdentityIntegrationMessage> record);
}
