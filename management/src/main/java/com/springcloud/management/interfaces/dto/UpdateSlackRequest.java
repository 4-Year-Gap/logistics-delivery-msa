package com.springcloud.management.interfaces.dto;


import com.springcloud.management.domain.entity.Contents;

import java.util.List;
import java.util.UUID;

public record UpdateSlackRequest(UUID slackId,
                                 String contents) {
}
