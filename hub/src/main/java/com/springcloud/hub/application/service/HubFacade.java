package com.springcloud.hub.application.service;

import com.springcloud.hub.application.dto.*;
import com.springcloud.hub.domain.entity.Hub;
import com.springcloud.hub.domain.repository.*;
import com.springcloud.hub.domain.service.HubService;
import com.springcloud.hub.infrastructure.dto.KakaoMapApiResponse;
import com.springcloud.hub.infrastructure.external.MapFinder;
import com.springcloud.hub.interfaces.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubFacade {
    private final HubReader hubReader;
    private final HubStore hubStore;
    private final MapFinder mapFinder;
    private final HubService hubService;

    /**
     * 검색한 주소지와 위도 경도를 반환(Kakao Map API)
     */
    public KakaoMapApiResponse getAddressLatitudeAndLongitude(FindAddressRequest requestDto, Pageable pageable) {
        FindAddressQuery findAddressQuery = FindAddressQuery.fromRequestParam(requestDto);
        return mapFinder.getLatitudeAndLongitude(findAddressQuery, pageable);
    }

    public FindHubQuery createHub(CreateHubRequest requestDto) {
        CreateHubCommand command = CreateHubCommand.fromCreateHubRequest(requestDto);
        return FindHubQuery.fromFindHubQuery(hubStore.save(command.toEntity()));
    }

    public ListHubQuery findHubs(SearchHubRequest command, Pageable pageable) {
        SearchHubQuery query = SearchHubQuery.fromSearchHubRequest(command);
        return ListHubQuery.fromEntities(hubReader.findAllHubs(query, pageable));
    }

    public FindHubQuery updateHub(UpdateHubRequest requestDto) {
        UpdateHubCommand command = UpdateHubCommand.fromUpdateHubRequest(requestDto);
        Hub updatedHub = command.toEntity(hubService.findById(command.id()));
        return FindHubQuery.fromFindHubQuery(hubStore.save(updatedHub));
    }

    public FindHubQuery deleteHub(DeleteHubRequest requestDto) {
        DeleteHubCommand command = DeleteHubCommand.fromDeleteHubRequest(requestDto);
        Hub updatedHub = command.toEntity(hubService.findById(command.id()));
        return FindHubQuery.fromFindHubQuery(hubStore.save(updatedHub));
    }
}