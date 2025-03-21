package com.springcloud.client.user.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NgrokConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DEFAULT_HOST = "localhost:19099"; // 예외 발생 시 사용할 기본 값

    @PostConstruct
    public void init() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject("http://127.0.0.1:4040/api/tunnels", String.class);

            // JSON 파싱 (Jackson 사용)
            JsonNode jsonNode = objectMapper.readTree(response);
            String ngrokUrl = jsonNode.path("tunnels").get(0).path("public_url").asText();

            // Ngrok URL을 환경 변수로 설정
            System.setProperty("My_host", ngrokUrl.replace("https://", "").replace("http://", ""));

            System.out.println("Ngrok URL 설정 완료: " + ngrokUrl);
        } catch (Exception e) {
            // 예외 발생 시 기본 호스트 값 설정
            System.setProperty("My_host", DEFAULT_HOST);
            System.err.println("Ngrok URL을 가져오는 데 실패하여 기본값(" + DEFAULT_HOST + ")을 사용합니다. 오류: " + e.getMessage());
        }
    }
}