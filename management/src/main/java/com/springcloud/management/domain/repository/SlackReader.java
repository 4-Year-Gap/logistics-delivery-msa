package com.springcloud.management.domain.repository;

import com.springcloud.management.application.dto.SearchSlackQuery;
import com.springcloud.management.domain.entity.Slack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SlackReader {
    Page<Slack> findByMessage(SearchSlackQuery searchHubQuery, Pageable pageable);
    Optional<Slack> findById(UUID slackId);
}
