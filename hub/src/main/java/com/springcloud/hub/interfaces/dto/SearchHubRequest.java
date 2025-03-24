package com.springcloud.hub.interfaces.dto;


import java.util.UUID;

public record SearchHubRequest(String address,
                               UUID hubId) {
}