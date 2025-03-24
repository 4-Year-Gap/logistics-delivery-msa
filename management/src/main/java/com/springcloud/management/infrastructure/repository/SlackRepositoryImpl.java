package com.springcloud.management.infrastructure.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.management.application.dto.SearchSlackQuery;
import com.springcloud.management.domain.entity.QSlack;
import com.springcloud.management.domain.entity.Slack;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SlackRepositoryImpl implements SlackRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Slack> findByMessage(SearchSlackQuery searchSlackQuery, Pageable pageable){
        QSlack slack = QSlack.slack;

        List<Slack> content = queryFactory
                .selectFrom(slack)
                .where(
                        slack.deletedAt.isNull(),
                        messageContains(searchSlackQuery.message())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(slack.count())
                .from(slack)
                .where(
                        slack.deletedAt.isNull(),
                        messageContains(searchSlackQuery.message())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression messageContains(String name) {
        return StringUtils.hasText(name)
                ? QSlack.slack.contents.message.containsIgnoreCase(name)
                : null;
    }
}
