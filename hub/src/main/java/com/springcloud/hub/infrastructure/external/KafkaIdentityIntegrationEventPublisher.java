package com.springcloud.hub.infrastructure.external;

import com.springcloud.hub.application.dto.CreateIdentityIntegrationCommand;
import com.springcloud.hub.application.dto.DeleteIdentityIntegrationCommand;
import com.springcloud.hub.application.dto.UpdateIdentityIntegrationCommand;
import com.springcloud.hub.infrastructure.dto.IdentityIntegrationCommand;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaIdentityIntegrationEventPublisher implements IdentityIntegrationEventPublisher {

    private final KafkaTemplate<String, IdentityIntegrationCommand> kafkaTemplate;

    private static final String topic = "integrated-user-topic";

    private String KEY_PREFIX = "HUB:";

    @Override
    public void publish(CreateIdentityIntegrationCommand integrationCommand) {
        kafkaTemplate.send(
                topic,
                KEY_PREFIX + "CREATE",
                IdentityIntegrationCommand
                        .builder()
                        .hubId(integrationCommand.hubId())
                        .userId(integrationCommand.userId())
                        .build()
        );
    }

    @Override
    public void publish(UpdateIdentityIntegrationCommand integrationCommand) {
        kafkaTemplate.send(
                topic,
                KEY_PREFIX + "UPDATE",
                IdentityIntegrationCommand
                        .builder()
                        .hubId(integrationCommand.hubId())
                        .userId(integrationCommand.userId())
                        .build()
        );
    }

    @Override
    public void publish(DeleteIdentityIntegrationCommand integrationCommand) {
        kafkaTemplate.send(
                topic,
                KEY_PREFIX + "DELETE",
                IdentityIntegrationCommand
                        .builder()
                        .hubId(integrationCommand.hubId())
                        .userId(null)
                        .build()
        );
    }

//    @KafkaListener(
//            groupId = "integrated-user-group",
//            topics = topic,
//            containerFactory = "kafkaListenerContainerFactory"
//    )
//    public void listen(ConsumerRecord<String, IdentityIntegrationCommand> record, Acknowledgment ack) {
//        System.out.println("Received message: " + record.value());
//
//        // 메시지 처리 완료 후 커밋
//        ack.acknowledge();
//    }
}
