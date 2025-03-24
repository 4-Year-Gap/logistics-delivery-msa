package com.springcloud.company.company.dto;

import com.springcloud.company.company.entity.Company;

import java.util.UUID;

public record CreateIdentityIntegrationCommand(UUID companyId,
                                               UUID userId){

    public static CreateIdentityIntegrationCommand fromEntity(Company company) {
        return new CreateIdentityIntegrationCommand(
                company.getId(),
                company.getUserId()
        );
    }
}
