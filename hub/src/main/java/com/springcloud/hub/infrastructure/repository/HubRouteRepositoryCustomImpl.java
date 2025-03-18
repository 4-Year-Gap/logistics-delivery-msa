package com.springcloud.hub.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.hub.application.HubDto;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.QHub;
import com.springcloud.hub.domain.entity.QHubRoute;
import com.springcloud.hub.infrastructure.dto.HubRouteDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class HubRouteRepositoryCustomImpl implements HubRouteRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<HubRouteDTO> findByFromHubWithToHub(HubDto hubDto) {
        QHubRoute hubRoute = QHubRoute.hubRoute;
        QHub toHubAlias = new QHub("toHubAlias");
        QHub fromHubAlias = new QHub("fromHubAlias");

        return queryFactory
                .select(Projections.fields(HubRouteDTO.class,
                                hubRoute.Id.as("id"),
                                hubRoute.toHub.Id.as("toHubId"),
                                hubRoute.fromHub.Id.as("fromHubId"),
                                hubRoute.toHub.as("toHub"),
                                hubRoute.fromHub.as("fromHub"),
                                hubRoute.moveDistance,
                                hubRoute.timeRequired))
                .from(hubRoute)
                .join(hubRoute.toHub, toHubAlias)
                .join(hubRoute.fromHub, fromHubAlias)
                .where(hubRoute.fromHub.Id.eq(hubDto.id()))
                .fetch();
    }
}
