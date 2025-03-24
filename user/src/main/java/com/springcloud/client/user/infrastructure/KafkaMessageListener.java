package com.springcloud.client.user.infrastructure;

import com.springcloud.client.user.application.IdentityIntegrationFacade;
import com.springcloud.client.user.domain.IdentityIntegrationCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageListener implements MessageListener {

    private final IdentityIntegrationFacade identityIntegrationFacade;

    @KafkaListener(groupId = "user-group", topics = "integrated-user-topic", containerFactory = "kafkaListenerContainerFactory")
    public void consumeMessage(ConsumerRecord<String, IdentityIntegrationMessage> record) {
        String key = record.key();
        IdentityIntegrationMessage message = record.value();
        IdentityIntegrationCommand command = message.toCommand(key);
        identityIntegrationFacade.manageIdentity(command);
    }
}