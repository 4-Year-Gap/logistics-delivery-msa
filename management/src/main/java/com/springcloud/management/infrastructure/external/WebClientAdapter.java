package com.springcloud.management.infrastructure.external;

import com.springcloud.management.application.dto.CreateSlackCommand;
import reactor.core.publisher.Mono;

public interface WebClientAdapter {
    Mono<String> sendRequestToGemini(CreateSlackCommand requestDto);
}
