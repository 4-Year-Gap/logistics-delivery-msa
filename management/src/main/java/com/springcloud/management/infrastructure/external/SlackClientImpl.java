package com.springcloud.management.infrastructure.external;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SlackClientImpl implements SlackClientAdapter {
    @Value(value = "${slack.key}")
    String slackKey;

    @Value(value = "${slack.channel}")
    String slackChannel;

    @Override
    public void sendSlackMessageToDeliveryChannel(String message) {
        try {
            MethodsClient methods = Slack.getInstance().methods(slackKey);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(slackChannel)
                    .text(message)
                    .build();

            ChatPostMessageResponse response = methods.chatPostMessage(request);

            if (!response.isOk()) {
                log.error("Slack 메시지 전송 실패: " + response.getError());
            }

            log.info("Slack " + slackChannel + " 에 메시지 보냄");
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
