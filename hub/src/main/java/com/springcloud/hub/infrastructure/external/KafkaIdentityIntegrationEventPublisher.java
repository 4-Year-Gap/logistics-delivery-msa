package com.springcloud.hub.infrastructure.external;

import com.springcloud.hub.application.dto.CreateIdentityIntegrationCommand;
import com.springcloud.hub.application.dto.DeleteIdentityIntegrationCommand;
import com.springcloud.hub.application.dto.UpdateIdentityIntegrationCommand;
import com.springcloud.hub.infrastructure.dto.IdentityIntegrationCommand;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
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
}
