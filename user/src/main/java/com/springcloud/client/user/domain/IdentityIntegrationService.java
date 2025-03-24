package com.springcloud.client.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class IdentityIntegrationService {

    private static final String HASH_TABLE_KEY = "identityIntegrationCache";
    private final RedisTemplate<String, IdentityIntegrationCacheData> redisTemplate;

    public void manageIdentity(IdentityIntegrationCommand command) {
        String fieldKey = command.getUserId().toString();

        MessageEventType eventType = MessageEventType.valueOf(command.getEventType());

        switch (eventType) {
            case CREATE -> createIdentity(command, fieldKey);
            case UPDATE -> updateIdentity(command, fieldKey);
            case DELETE -> deleteIdentity(command, fieldKey);
        }
    }

    private void createIdentity(IdentityIntegrationCommand command, String fieldKey) {
        IdentityIntegrationCacheData newData = command.toCacheData();
        redisTemplate.opsForHash().put(HASH_TABLE_KEY, fieldKey, newData);
    }

    private void updateIdentity(IdentityIntegrationCommand command, String fieldKey) {
        IdentityIntegrationCacheData existingData = (IdentityIntegrationCacheData) redisTemplate.opsForHash().get(HASH_TABLE_KEY, fieldKey);

        MessageDomain domain = MessageDomain.valueOf(command.getDomain());

        switch (domain) {
            case COMPANY -> existingData.setCompanyId(command.getCompanyId());
            case HUB -> existingData.setHubId(command.getHubId());
            case DELIVERY -> existingData.setDeliveryId(command.getDeliveryId());
            case ORDER -> {
                List<UUID> orderIdList = existingData.getOrderIdList();
                orderIdList.addAll(command.getOrderIdList());
                existingData.setOrderIdList(orderIdList);
            }
        }

        redisTemplate.opsForHash().put(HASH_TABLE_KEY, fieldKey, existingData);
    }

    private void deleteIdentity(IdentityIntegrationCommand command, String fieldKey) {
        IdentityIntegrationCacheData existingData = (IdentityIntegrationCacheData) redisTemplate.opsForHash().get(HASH_TABLE_KEY, fieldKey);

        MessageDomain domain = MessageDomain.valueOf(command.getDomain());

        switch (domain) {
            case COMPANY -> existingData.setCompanyId(null);
            case HUB -> existingData.setHubId(null);
            case DELIVERY -> existingData.setDeliveryId(null);
            case ORDER -> {
                List<UUID> orderIdList = existingData.getOrderIdList();
                orderIdList.removeAll(command.getOrderIdList());
                existingData.setOrderIdList(orderIdList);
            }
        }

        redisTemplate.opsForHash().put(HASH_TABLE_KEY, fieldKey, existingData);
    }
}
