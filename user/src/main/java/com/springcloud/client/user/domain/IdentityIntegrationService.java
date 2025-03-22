package com.springcloud.client.user.domain;

import com.springcloud.client.user.infrastructure.IdentityIntegrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IdentityIntegrationService {

    private final RedisTemplate<String, IdentityIntegrationDto> redisTemplate;
    private static final String HASH_TABLE_KEY = "identityIntegrationCache";

    public void addIdentity(IdentityIntegrationDto dto) {
        String fieldKey = dto.getUserId().toString();

        // Redis에 기존 데이터가 있는지 확인
        IdentityIntegrationDto existingData = (IdentityIntegrationDto) redisTemplate.opsForHash().get("identityIntegrationCache", fieldKey);

        if (existingData != null) {
            // 기존 데이터가 있으면 업데이트
            if (dto.getHubId() != null) existingData.setHubId(dto.getHubId());
            if (dto.getCompanyId() != null) existingData.setCompanyId(dto.getCompanyId());
            if (dto.getOrderId() != null) existingData.setOrderId(dto.getOrderId());
            if (dto.getDeliveryId() != null) existingData.setDeliveryId(dto.getDeliveryId());

            redisTemplate.opsForValue().set(fieldKey, existingData, 3, TimeUnit.DAYS);
        } else {
            // 없으면 새로 저장
            redisTemplate.opsForHash().put(HASH_TABLE_KEY, fieldKey, dto);
        }
    }
}
