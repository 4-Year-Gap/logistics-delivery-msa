package com.springcloud.hub.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.QHub;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class HubRepositoryCustomImpl implements HubRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Hub> findHubById(UUID hubId) {
        QHub hub = QHub.hub;

        return Optional.ofNullable(
                queryFactory.selectFrom(hub)
                        .where(hub.Id.eq(hubId).and(hub.isDeleted.eq(false)))
                        .fetchOne()
        );
    }

    @Override
    public List<Hub> findAllHubs() {
        QHub hub = QHub.hub;
        return queryFactory.selectFrom(hub)
                .where(hub.isDeleted.eq(false))
                .fetch();
    }
}
