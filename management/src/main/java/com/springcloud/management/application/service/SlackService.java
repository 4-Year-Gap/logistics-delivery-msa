package com.springcloud.management.application.service;


import com.springcloud.management.interfaces.dto.CreateSlackRequest;
import com.springcloud.management.interfaces.dto.CreateSlackResponse;

import java.util.UUID;

public interface SlackService {
    CreateSlackResponse saveSlackMessage(CreateSlackRequest requestDto, UUID userId);
}
