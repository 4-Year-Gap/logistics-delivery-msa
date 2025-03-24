package com.springcloud.management.interfaces.dto;


import java.util.UUID;

public record SearchSlackRequest(UUID slackId,
                                 String message) {
}
