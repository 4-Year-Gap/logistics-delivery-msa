package com.springcloud.hub.infrastructure.repository;

import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.SearchHubRouteQuery;
import com.springcloud.hub.application.dto.HubRouteListCommand;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.infrastructure.dto.GetHubRouteQuery;
import com.springcloud.hub.infrastructure.dto.ListHubRouteQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HubRouteRepositoryCustom {
    HubRouteListCommand saveAll(List<HubRoute> routes);
    List<GetHubRouteQuery> findByFromHubWithToHub(FindHubQuery hub);
    Page<ListHubRouteQuery> findByAddressAndLatitudeAndLongitude(SearchHubRouteQuery hubRoute, Pageable pageable);
}
