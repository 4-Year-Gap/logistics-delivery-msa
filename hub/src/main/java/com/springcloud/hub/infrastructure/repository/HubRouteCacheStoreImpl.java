package com.springcloud.hub.infrastructure.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.GetHubRouteCacheQuery;
import com.springcloud.hub.application.dto.GetHubRouteQuery;
import com.springcloud.hub.domain.repository.HubRouteCacheStore;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class HubRouteCacheStoreImpl implements HubRouteCacheStore {

    private final RedisTemplate<String, List<GetHubRouteQuery>> hubRouteTemplate;
    private static final String HASH_KEY = "hubRouteCache";

    public HubRouteCacheStoreImpl(RedisTemplate<String, List<GetHubRouteQuery>> hubRouteTemplate) {
        this.hubRouteTemplate = hubRouteTemplate;
    }

    @Override
    public void saveShortestPath(FindHubQuery fromHub, FindHubQuery toHub, List<GetHubRouteQuery> route) {
        String cacheKey = fromHub.id() + ":" + toHub.id();
        hubRouteTemplate.opsForHash().put(HASH_KEY, cacheKey, route);
    }

    @Override
    public List<GetHubRouteQuery> getShortestPath(GetHubRouteCacheQuery getHubRouteCacheQuery) {
        String cacheKey = getHubRouteCacheQuery.startHubId() + ":" + getHubRouteCacheQuery.goalHubId();
        Object result = hubRouteTemplate.opsForHash().get(HASH_KEY, cacheKey);

        if (result instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<GetHubRouteQuery> list = (List<GetHubRouteQuery>) result;
            return list;
        }

        return Collections.emptyList();
    }
}
