package com.springcloud.client.user.domain;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdentityIntegrationCacheData implements Serializable {

    private UUID userId;
    private List<UUID> orderIdList;
    private UUID hubId;
    private UUID companyId;
    private UUID deliveryId;
}
