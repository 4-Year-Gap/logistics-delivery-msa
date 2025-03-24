package com.springcloud.hub.infrastructure.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.hub.application.dto.FindHubQuery;
import com.springcloud.hub.application.dto.SearchHubQuery;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.entity.QHub;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRepositoryCustomImpl implements HubRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Hub> findById(UUID hubId) {
        QHub hub = QHub.hub;

        return Optional.ofNullable(
                queryFactory.selectFrom(hub)
                        .where(hub.Id.eq(hubId).and(hub.deletedAt.isNull()))
                        .fetchOne()
        );
    }

    @Override
    public List<Hub> findAllHubs() {
        QHub hub = QHub.hub;
        return queryFactory.selectFrom(hub)
                .where(hub.deletedAt.isNull())
                .fetch();
    }

    @Override
    public Page<Hub> findAllHubs(SearchHubQuery searchHubQuery, Pageable pageable) {
        QHub hub = QHub.hub;

        List<Hub> content = queryFactory
                .selectFrom(hub)
                .where(
                        hub.deletedAt.isNull(),
                        nameContains(searchHubQuery.name()),
                        hubIdEquals(searchHubQuery.hubId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(hub.count())
                .from(hub)
                .where(
                        hub.deletedAt.isNull(),
                        nameContains(searchHubQuery.name()),
                        hubIdEquals(searchHubQuery.hubId())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 검색 조건: name이 존재하면 부분 검색
     */
    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? QHub.hub.name.containsIgnoreCase(name) : null;
    }

    /**
     * 검색 조건: hubId가 존재하면 해당 hubId로 검색
     */
    private BooleanExpression hubIdEquals(UUID hubId) {
        return (hubId != null) ? QHub.hub.Id.eq(hubId) : null;
    }
}
