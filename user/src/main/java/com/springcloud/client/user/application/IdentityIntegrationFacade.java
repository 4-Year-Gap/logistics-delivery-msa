package com.springcloud.client.user.application;

import com.springcloud.client.user.domain.IdentityIntegrationService;
import com.springcloud.client.user.infrastructure.IdentityIntegrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentityIntegrationFacade {

    private final IdentityIntegrationService identityIntegrationService;

    public void addIdentity(IdentityIntegrationDto dto) {
        identityIntegrationService.addIdentity(dto);
    }
}
