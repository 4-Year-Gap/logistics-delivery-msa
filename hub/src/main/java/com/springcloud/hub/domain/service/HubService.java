package com.springcloud.hub.domain.service;

import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.repository.HubReader;
import com.springcloud.hub.domain.repository.HubRouteReader;
import com.springcloud.hub.interfaces.exception.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {
    private final HubReader hubReader;

    /**
     * 주어진 허브 ID를 기반으로 허브를 조회하고, 존재하지 않으면 예외를 던짐
     */
    public Hub findById(UUID hubId) {
        return hubReader.findById(hubId)
                .orElseThrow(() -> new CustomNotFoundException("허브 정보를 찾을 수 없습니다."));
    }
}