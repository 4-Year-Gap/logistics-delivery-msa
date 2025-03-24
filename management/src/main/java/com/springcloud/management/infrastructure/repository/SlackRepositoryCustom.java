package com.springcloud.management.infrastructure.repository;

import com.springcloud.management.application.dto.SearchSlackQuery;
import com.springcloud.management.domain.entity.Slack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SlackRepositoryCustom {
    Page<Slack> findByMessage(SearchSlackQuery searchHubQuery, Pageable pageable);
}
