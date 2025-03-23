package com.springcloud.management.application.service;


import com.springcloud.management.application.dto.CreateSlackCommand;
import com.springcloud.management.domain.entity.Slack;
import com.springcloud.management.domain.repository.SlackStore;
import com.springcloud.management.infrastructure.external.SlackClientAdapter;
import com.springcloud.management.infrastructure.external.WebClientAdapter;
import com.springcloud.management.interfaces.dto.CreateSlackRequest;
import com.springcloud.management.interfaces.dto.CreateSlackResponse;
import com.springcloud.management.interfaces.exception.SlackException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlackServiceImpl implements SlackService {

    private final SlackStore slackStore;
    private final WebClientAdapter webClientAdapter;
    private final SlackClientAdapter slackClientAdapter;

    @Override
    public CreateSlackResponse saveSlackMessage(CreateSlackRequest requestDto, UUID userId) {
        CreateSlackCommand createSlackCommand = CreateSlackCommand.fromCreateSlackRequest(requestDto);
        return webClientAdapter.sendRequestToGemini(createSlackCommand)
                .onErrorMap(error -> new SlackException("Gemini API 호출 중 예외 발생"))
                .map(resultMessage -> {
                    Slack slack = Slack.createSlack(userId, resultMessage);
                    Slack savedSlack = slackStore.save(slack);
                    slackClientAdapter.sendSlackMessageToDeliveryChannel(savedSlack.getContents().getMessage());
                    return CreateSlackResponse.fromEntity(savedSlack);
                }).block();
    }
}
