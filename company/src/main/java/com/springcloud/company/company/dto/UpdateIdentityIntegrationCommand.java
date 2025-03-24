package com.springcloud.company.company.dto;

import com.springcloud.company.company.entity.Company;

import java.util.UUID;

public record UpdateIdentityIntegrationCommand(UUID companyId,
                                               UUID userId){

    public static UpdateIdentityIntegrationCommand fromEntity(Company company) {
        return new UpdateIdentityIntegrationCommand(
                company.getId(),
                company.getUserId()
        );
    }
}
