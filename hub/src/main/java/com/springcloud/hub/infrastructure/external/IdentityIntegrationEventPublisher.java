package com.springcloud.hub.infrastructure.external;

import com.springcloud.hub.application.dto.CreateIdentityIntegrationCommand;
import com.springcloud.hub.application.dto.DeleteIdentityIntegrationCommand;
import com.springcloud.hub.application.dto.UpdateIdentityIntegrationCommand;

public interface IdentityIntegrationEventPublisher {
    void publish(CreateIdentityIntegrationCommand integrationCommand);
    void publish(UpdateIdentityIntegrationCommand integrationCommand);
    void publish(DeleteIdentityIntegrationCommand integrationCommand);
}
