package com.springcloud.management.application.service;


import com.springcloud.management.application.dto.*;
import com.springcloud.management.domain.entity.Slack;
import com.springcloud.management.domain.repository.SlackReader;
import com.springcloud.management.domain.repository.SlackStore;
import com.springcloud.management.domain.service.UserRole;
import com.springcloud.management.infrastructure.external.SlackClientAdapter;
import com.springcloud.management.infrastructure.external.WebClientAdapter;
import com.springcloud.management.interfaces.dto.*;
import com.springcloud.management.interfaces.exception.AccessDeniedException;
import com.springcloud.management.interfaces.exception.SlackException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlackServiceImpl implements SlackService {

    private final SlackStore slackStore;
    private final SlackReader slackReader;
    private final WebClientAdapter webClientAdapter;
    private final SlackClientAdapter slackClientAdapter;

    @Override
    public CreateSlackResponse saveSlackMessage(CreateSlackRequest requestDto, UUID userId) {
        CreateSlackCommand createSlackCommand = CreateSlackCommand.fromCreateSlackRequest(requestDto);
        return webClientAdapter.sendRequestToGemini(createSlackCommand)
                .onErrorMap(error -> new SlackException("Gemini API 호출 중 예외 발생"))
                .map(resultMessage -> {
                    Slack slack = Slack.createSlack(userId, resultMessage);
                    slack.create(String.valueOf(userId));
                    Slack savedSlack = slackStore.save(slack);
                    slackClientAdapter.sendSlackMessageToDeliveryChannel(savedSlack.getContents().getMessage());
                    return CreateSlackResponse.fromEntity(savedSlack);
                }).block();
    }

    @Override
    public ListSlackQuery searchSlackMessage(SearchSlackRequest requestDto, Pageable pageable, UUID userId, UserRole role) {
        if (!UserRole.MASTER.equals(role)) {
            throw new AccessDeniedException("슬랙 조회 권한이 없습니다.");
        }

        SearchSlackQuery query = SearchSlackQuery.fromSearchHubRequest(requestDto);

        return ListSlackQuery.fromEntities(slackReader.findByMessage(query, pageable));
    }

    @Override
    public UpdateSlackResponse updateSlackMessage(UpdateSlackRequest requestDto, UUID userId, UserRole role) {
        if (!UserRole.MASTER.equals(role)) {
            throw new AccessDeniedException("슬랙 수정 권한이 없습니다.");
        }

        UpdateSlackCommand command = UpdateSlackCommand.fromUpdateHubRequest(requestDto, userId);

        Slack slack = command.toEntity(slackReader.findById(requestDto.slackId()).orElseThrow(
                () -> new IllegalArgumentException("Slack ID를 찾을수 없습니다.")));

        Slack updateSlack = slackStore.save(slack);

        return UpdateSlackResponse.fromEntity(updateSlack);
    }

    @Override
    public DeleteSlackResponse deleteSlackMessage(DeleteSlackRequest requestDto, UUID userId, UserRole role) {
        if (!UserRole.MASTER.equals(role)) {
            throw new AccessDeniedException("슬랙 수정 권한이 없습니다.");
        }

        DeleteSlackCommand command = DeleteSlackCommand.fromDeleteHubRequest(requestDto, userId);

        Slack slack = command.toEntity(slackReader.findById(requestDto.slackId()).orElseThrow(
                () -> new IllegalArgumentException("Slack ID를 찾을수 없습니다.")));

        Slack deleteSlack = slackStore.save(slack);

        return DeleteSlackResponse.fromEntity(deleteSlack);
    }
}
