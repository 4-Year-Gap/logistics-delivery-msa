package com.springcloud.company.company.infrastructure.external;

import com.springcloud.company.company.dto.CreateIdentityIntegrationCommand;
import com.springcloud.company.company.dto.DeleteIdentityIntegrationCommand;
import com.springcloud.company.company.dto.UpdateIdentityIntegrationCommand;
import com.springcloud.company.company.infrastructure.dto.IdentityIntegrationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaIdentityIntegrationEventPublisher implements IdentityIntegrationEventPublisher {

    private final KafkaTemplate<String, IdentityIntegrationCommand> kafkaTemplate;

    private static final String topic = "integrated-user-topic";

    private final String KEY_PREFIX = "COMPANY:";

    @Override
    public void publish(CreateIdentityIntegrationCommand integrationCommand) {
        kafkaTemplate.send(
                topic,
                KEY_PREFIX + "CREATE",
                IdentityIntegrationCommand
                        .builder()
                        .companyId(integrationCommand.companyId())
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
                        .companyId(integrationCommand.companyId())
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
                        .companyId(integrationCommand.companyId())
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
