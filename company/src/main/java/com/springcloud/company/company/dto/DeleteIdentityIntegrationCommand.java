package com.springcloud.company.company.dto;

import com.springcloud.company.company.entity.Company;

import java.util.UUID;

public record DeleteIdentityIntegrationCommand(UUID companyId){

    public static DeleteIdentityIntegrationCommand fromEntity(Company company) {
        return new DeleteIdentityIntegrationCommand(
                company.getId()
        );
    }
}
