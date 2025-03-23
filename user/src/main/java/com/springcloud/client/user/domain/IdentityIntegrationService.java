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

        if (command.getEventType().equals("CREATE")) {
            createIdentity(command, fieldKey);
        }
        if (command.getEventType().equals("UPDATE")) {
            updateIdentity(command, fieldKey);
        }
        if (command.getEventType().equals("DELETE")) {
            deleteIdentity(command, fieldKey);
        }
    }

    private void createIdentity(IdentityIntegrationCommand command, String fieldKey) {
        IdentityIntegrationCacheData identityIntegrationCacheData = command.toCacheData();
        redisTemplate.opsForHash().put(HASH_TABLE_KEY, fieldKey, identityIntegrationCacheData);
    }

    private void updateIdentity(IdentityIntegrationCommand command, String fieldKey) {
        IdentityIntegrationCacheData existingData = (IdentityIntegrationCacheData) redisTemplate.opsForHash().get(HASH_TABLE_KEY, fieldKey);

        if (command.getHubId() != null) {
            existingData.setHubId(command.getHubId());
        }
        if (command.getCompanyId() != null) {
            existingData.setCompanyId(command.getCompanyId());
        }
        if (command.getDeliveryId() != null) {
            existingData.setDeliveryId(command.getDeliveryId());
        }
        if (command.getOrderIdList() != null) {
            List<UUID> orderIdList = existingData.getOrderIdList();
            orderIdList.addAll(command.getOrderIdList());
            existingData.setOrderIdList(orderIdList);
        }

        redisTemplate.opsForHash().put(HASH_TABLE_KEY, fieldKey, existingData);
    }

    private void deleteIdentity(IdentityIntegrationCommand command, String fieldKey) {
        IdentityIntegrationCacheData existingData = (IdentityIntegrationCacheData) redisTemplate.opsForHash().get(HASH_TABLE_KEY, fieldKey);

        if (command.getDomain().equals("COMPANY")) {
            existingData.setCompanyId(null);
        }
        if (command.getDomain().equals("HUB")) {
            existingData.setHubId(null);
        }
        if (command.getDomain().equals("DELIVERY")) {
            existingData.setDeliveryId(null);
        }
        if (command.getDomain().equals("ORDER")) {
            List<UUID> orderIdList = existingData.getOrderIdList();
            orderIdList.removeAll(command.getOrderIdList());
            existingData.setOrderIdList(orderIdList);
        }

        redisTemplate.opsForHash().put(HASH_TABLE_KEY, fieldKey, existingData);
    }
}
