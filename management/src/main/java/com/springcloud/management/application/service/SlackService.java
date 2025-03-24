package com.springcloud.management.application.service;


import com.springcloud.management.application.dto.ListSlackQuery;
import com.springcloud.management.domain.service.UserRole;
import com.springcloud.management.interfaces.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SlackService {
    CreateSlackResponse saveSlackMessage(CreateSlackRequest requestDto, UUID userId);
    ListSlackQuery searchSlackMessage(SearchSlackRequest requestDto, Pageable pageable, UUID userId, UserRole role);
    UpdateSlackResponse updateSlackMessage(UpdateSlackRequest requestDto, UUID userId, UserRole role);
    DeleteSlackResponse deleteSlackMessage(DeleteSlackRequest requestDto, UUID userId, UserRole role);
}
