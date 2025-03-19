package com.springcloud.hub.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.HubRouteListCommand;
import com.springcloud.hub.domain.entity.HubRoute;
import com.springcloud.hub.domain.entity.QHub;
import com.springcloud.hub.domain.entity.QHubRoute;
import com.springcloud.hub.infrastructure.dto.FindHubRouteQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HubRouteRepositoryCustomImpl implements HubRouteRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FindHubRouteQuery> findByFromHubWithToHub(FindHubQuery findHubQuery) {
        QHubRoute hubRoute = QHubRoute.hubRoute;
        QHub toHubAlias = new QHub("toHubAlias");
        QHub fromHubAlias = new QHub("fromHubAlias");

        return queryFactory
                .select(Projections.fields(FindHubRouteQuery.class,
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
                .where(hubRoute.fromHub.Id.eq(findHubQuery.id()))
                .fetch();
    }

    public HubRoute initializeHubRoute(HubRoute route) {
        // 연관된 객체들을 명시적으로 초기화
        Hibernate.initialize(route.getFromHub());
        Hibernate.initialize(route.getToHub());
        // 추가적인 초기화가 필요한 연관 객체가 있다면 여기서 처리
        return route;
    }

    @Transactional
    @Override
    public HubRouteListCommand saveAll(List<HubRoute> routes) {
        List<HubRoute> savedRoutes = new ArrayList<>();

        for (int i = 0; i < routes.size(); i++) {
            HubRoute route = routes.get(i);

            // 연관된 객체들을 초기화
            route = initializeHubRoute(route);

            if (route.getId() == null) {  // 새로운 엔티티
                entityManager.persist(route);
            } else {  // 기존 엔티티
                route = entityManager.merge(route);
            }

            savedRoutes.add(route);

            // **Batch Insert 최적화**: 일정 개수마다 플러시 & 클리어
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        return HubRouteListCommand.fromEntities(savedRoutes);
    }


}
