package com.springcloud.hub.application.service;

import com.springcloud.hub.application.dto.*;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.repository.*;
import com.springcloud.hub.domain.service.HubService;
import com.springcloud.hub.domain.service.UserRole;
import com.springcloud.hub.infrastructure.dto.KakaoMapApiResponse;
import com.springcloud.hub.infrastructure.external.IdentityIntegrationEventPublisher;
import com.springcloud.hub.infrastructure.external.MapFinder;
import com.springcloud.hub.interfaces.dto.*;
import com.springcloud.hub.interfaces.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubFacade {
    private final HubReader hubReader;
    private final HubStore hubStore;
    private final MapFinder mapFinder;
    private final HubService hubService;
    private final IdentityIntegrationEventPublisher eventPublisher;

    /**
     * 검색한 주소지와 위도 경도를 반환(Kakao Map API)
     */
    public KakaoMapApiResponse getAddressLatitudeAndLongitude(FindAddressRequest requestDto, Pageable pageable) {
        FindAddressQuery findAddressQuery = FindAddressQuery.fromRequestParam(requestDto);
        return mapFinder.getLatitudeAndLongitude(findAddressQuery, pageable);
    }

    public FindHubQuery createHub(CreateHubRequest requestDto, UUID userId, UserRole role) {
        if (!UserRole.MASTER.equals(role)) {
            throw new AccessDeniedException("허브 생성 권한이 없습니다.");
        }

        CreateHubCommand command = CreateHubCommand.fromCreateHubRequest(requestDto, userId);
        Hub createHub = hubStore.save(command.toEntity());

        if (userId != null) {
            CreateIdentityIntegrationCommand integrationCommand = CreateIdentityIntegrationCommand.fromEntity(createHub);
            eventPublisher.publish(integrationCommand);
        }

        return FindHubQuery.fromFindHubQuery(createHub);
    }

    public ListHubQuery findHubs(SearchHubRequest command, Pageable pageable) {
        SearchHubQuery query = SearchHubQuery.fromSearchHubRequest(command);
        return ListHubQuery.fromEntities(hubReader.findAllHubs(query, pageable));
    }

    public FindHubQuery updateHub(UpdateHubRequest requestDto, UUID userId, UserRole role) {
        if (!UserRole.MASTER.equals(role)) {
            throw new AccessDeniedException("허브 수정 권한이 없습니다.");
        }

        UpdateHubCommand command = UpdateHubCommand.fromUpdateHubRequest(requestDto, userId);

        Hub hub = command.toEntity(hubService.findById(command.id()));
        Hub updateHub = hubStore.save(hub);

        if (command.userId() != null) {
            UpdateIdentityIntegrationCommand integrationCommand = UpdateIdentityIntegrationCommand.fromEntity(updateHub);
            eventPublisher.publish(integrationCommand);
        }

        return FindHubQuery.fromFindHubQuery(updateHub);
    }

    public FindHubQuery deleteHub(DeleteHubRequest requestDto, UUID userId, UserRole role) {
        if (!UserRole.MASTER.equals(role)) {
            throw new AccessDeniedException("허브 삭제 권한이 없습니다.");
        }

        DeleteHubCommand command = DeleteHubCommand.fromDeleteHubRequest(requestDto, userId);
        Hub hub = command.toEntity(hubService.findById(command.id()));
        Hub updateHub = hubStore.save(hub);

        DeleteIdentityIntegrationCommand integrationCommand = DeleteIdentityIntegrationCommand.fromEntity(updateHub);
        eventPublisher.publish(integrationCommand);

        return FindHubQuery.fromFindHubQuery(updateHub);
    }
}