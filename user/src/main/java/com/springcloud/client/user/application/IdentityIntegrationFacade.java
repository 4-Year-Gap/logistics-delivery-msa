package com.springcloud.client.user.application;

import com.springcloud.client.user.domain.IdentityIntegrationCommand;
import com.springcloud.client.user.domain.IdentityIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentityIntegrationFacade {

    private final IdentityIntegrationService identityIntegrationService;

    public void manageIdentity(IdentityIntegrationCommand command) {
        identityIntegrationService.manageIdentity(command);
    }
}
