package com.springcloud.hub.application.service;

import com.springcloud.hub.application.dto.*;
import com.springcloud.hub.domain.repository.*;
import com.springcloud.hub.interfaces.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubFacade {
    private final HubReader hubReader;
    private final HubStore hubStore;

    public FindHubQuery createHub(CreateHubRequest requestDto) {
        CreateHubCommand command = CreateHubCommand.fromCreateHubRequest(requestDto);
        return FindHubQuery.fromFindHubQuery(hubStore.save(command.toEntity()));
    }

    public ListHubQuery findHubs(FindHubRequest command, Pageable pageable) {
        FindHubQuery query = FindHubQuery.fromFindHubRequest(command);
        return ListHubQuery.fromEntities(hubReader.findAllHubs(query, pageable));
    }

    public FindHubQuery updateHub(UpdateHubRequest requestDto) {
        UpdateHubCommand command = UpdateHubCommand.fromUpdateHubRequest(requestDto);
        return FindHubQuery.fromFindHubQuery(hubStore.save(command.toEntity()));
    }

    public void deleteHub(DeleteHubRequest requestDto) {
        DeleteHubCommand command = DeleteHubCommand.fromDeleteHubRequest(requestDto);
        hubStore.delete(command.toEntity());
    }
}