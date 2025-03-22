package com.springcloud.company.company.infrastructure.external;

import com.springcloud.company.company.dto.CreateIdentityIntegrationCommand;
import com.springcloud.company.company.dto.DeleteIdentityIntegrationCommand;
import com.springcloud.company.company.dto.UpdateIdentityIntegrationCommand;

public interface IdentityIntegrationEventPublisher {
    void publish(CreateIdentityIntegrationCommand integrationCommand);
    void publish(UpdateIdentityIntegrationCommand integrationCommand);
    void publish(DeleteIdentityIntegrationCommand integrationCommand);
}
