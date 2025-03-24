package com.springcloud.management.infrastructure.external;

import com.springcloud.management.application.dto.CreateSlackCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientImpl implements WebClientAdapter {

    private final WebClient webClient;

    @Value("${ai.api.key}")
    private String apiKey;
    @Value("${ai.api.url}")
    private String aiUrl;

    public Mono<String> sendRequestToGemini(CreateSlackCommand requestDto) {
        String prompt = generatePrompt(requestDto);
        String url = aiUrl + apiKey;

        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(generateRequestBody(prompt))) // 요청 본문 설정
                .retrieve()
                .bodyToMono(String.class) // 응답 처리
                .map(response -> {
                    String deadline = extractDeadline(response);
                    return generateResultMessage(requestDto, deadline);
                });
    }

    private String generatePrompt(CreateSlackCommand requestDto) {
        StringBuilder promptBuilder = getStringBuilder(requestDto);
        // 추가적인 인스트럭션
        promptBuilder.append("\n").append(AiPromptEnum.PROMPT_INSTRUCTION.getPromptTemplate());

        return promptBuilder.toString();
    }

    private String generateResultMessage(CreateSlackCommand requestDto, String deadline) {
        StringBuilder promptBuilder = getStringBuilder(requestDto);
        promptBuilder.append(deadline);

        return promptBuilder.toString();
    }

    private Map<String, Object> generateRequestBody(String prompt) {
        // 텍스트 부분 생성
        Map<String, String> textPart = new HashMap<>();
        textPart.put("text", prompt);

        // parts 배열 생성
        List<Map<String, String>> parts = new ArrayList<>();
        parts.add(textPart);

        // 내용 객체 생성
        Map<String, Object> content = new HashMap<>();
        content.put("parts", parts);

        // contents 배열 생성
        List<Map<String, Object>> contents = new ArrayList<>();
        contents.add(content);

        // 최상위 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", contents);

        return requestBody;
    }
    public String extractDeadline(String aiResponse) {
        String pattern = "위 내용을 기반으로 도출된 ";
        int startIdx = aiResponse.indexOf(pattern);
        if (startIdx != -1) {
            int endIdx = aiResponse.indexOf("입니다.", startIdx);
            if (endIdx != -1) {
                return aiResponse.substring(startIdx, endIdx + "입니다.".length());
            }
        }
        return "출발 시간을 계산할 수 없습니다.";  // 응답에서 실패한 경우
    }

    private static StringBuilder getStringBuilder(CreateSlackCommand requestDto) {
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append(AiPromptEnum.ORDER_NUMBER.getPromptTemplate()).append(requestDto.orderNumber()).append("\n");
        promptBuilder.append(AiPromptEnum.COMPANY_NAME.getPromptTemplate()).append(requestDto.companyName()).append("\n");
        promptBuilder.append(AiPromptEnum.PRODUCT_INFO.getPromptTemplate()).append(requestDto.productInfo()).append("\n");
        promptBuilder.append(AiPromptEnum.ORDER_REQUEST.getPromptTemplate()).append(requestDto.orderRequest()).append("\n");
        promptBuilder.append(AiPromptEnum.FROM_HUB_NAME.getPromptTemplate()).append(requestDto.fromHubName()).append("\n");
        promptBuilder.append(AiPromptEnum.STOP_OVER_HUB_NAMES.getPromptTemplate()).append(String.join(", ", requestDto.stopoverHubNames())).append("\n");
        promptBuilder.append(AiPromptEnum.TO_HUB_NAME.getPromptTemplate()).append(requestDto.toHubName()).append("\n");
        promptBuilder.append(AiPromptEnum.DELIVERY_USERS.getPromptTemplate()).append(String.join(", ", requestDto.deliveryUsers())).append("\n");

        return promptBuilder;
    }
}
