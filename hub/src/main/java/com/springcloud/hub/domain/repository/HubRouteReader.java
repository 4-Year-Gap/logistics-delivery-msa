package com.springcloud.hub.domain.repository;

import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.SearchHubRouteQuery;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.infrastructure.dto.GetHubRouteQuery;
import com.springcloud.hub.infrastructure.dto.ListHubRouteQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRouteReader {
    Optional<HubRoute> findById(UUID hubId);
    List<GetHubRouteQuery> findByFromHubWithToHub(FindHubQuery hub);
    Page<ListHubRouteQuery> findByAddressAndLatitudeAndLongitude(SearchHubRouteQuery hubRoute, Pageable pageable);
}
