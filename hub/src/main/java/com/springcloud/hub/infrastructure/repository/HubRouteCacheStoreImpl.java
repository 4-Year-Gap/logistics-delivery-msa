package com.springcloud.hub.infrastructure.repository;

import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.GetHubRouteQuery;
import com.springcloud.hub.domain.repository.HubRouteCacheStore;
import com.springcloud.hub.interfaces.FindHubRouteRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
public class HubRouteCacheStoreImpl implements HubRouteCacheStore {

    private final RedisTemplate<String, List<GetHubRouteQuery>> hubRouteTemplate;
    private static final String CACHE_PREFIX = "hubRouteCache:";

    public HubRouteCacheStoreImpl(RedisTemplate<String, List<GetHubRouteQuery>> hubRouteTemplate) {
        this.hubRouteTemplate = hubRouteTemplate;
    }

    @Override
    public void saveShortestPath(FindHubQuery fromHub, FindHubQuery toHub, List<GetHubRouteQuery> route) {
        String cacheKey = CACHE_PREFIX + fromHub.id() + ":" + toHub.id();
        hubRouteTemplate.opsForValue().set(cacheKey, route, Duration.ofMinutes(100));
    }

    @Override
    public List<GetHubRouteQuery> getShortestPath(FindHubRouteRequest fromHub, FindHubRouteRequest toHub) {
        String cacheKey = CACHE_PREFIX + fromHub.startHubId() + ":" + toHub.goalHubId();
        return hubRouteTemplate.opsForValue().get(cacheKey);
    }
}
