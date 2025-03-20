package com.springcloud.hub.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NgrokConfig {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DEFAULT_HOST = "localhost:19091";

    private final String applicationName;

    public NgrokConfig(Environment environment) {
        this.applicationName = environment.getProperty("spring.application.name");
    }

    @PostConstruct
    public void init() {
        setNgrokUrlByName(applicationName);
    }

    public void setNgrokUrlByName(String tunnelName) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject("http://127.0.0.1:4040/api/tunnels", String.class);

            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode tunnelsNode = rootNode.path("tunnels");

            if (tunnelsNode.isArray()) {
                String ngrokUrl = null;

                if (tunnelName != null && !tunnelName.isEmpty()) {
                    for (JsonNode tunnel : tunnelsNode) {
                        String name = tunnel.path("name").asText();
                        if (name.equals(tunnelName)) {
                            ngrokUrl = tunnel.path("public_url").asText();
                            break;
                        }
                    }

                    if (ngrokUrl == null) {
                        System.err.println("이름이 '" + tunnelName + "'인 터널을 찾을 수 없습니다. 기본 호스트를 사용합니다.");
                        System.setProperty("My_host", DEFAULT_HOST);
                        return;
                    }
                } else {
                    if (!tunnelsNode.isEmpty()) {
                        ngrokUrl = tunnelsNode.get(0).path("public_url").asText();
                    }
                }

                if (ngrokUrl != null) {
                    String host = ngrokUrl.replace("https://", "").replace("http://", "");
                    System.setProperty("My_host", host);
                    System.out.println("Ngrok URL이 성공적으로 설정되었습니다: " + ngrokUrl);
                    return;
                }
            }

            throw new RuntimeException("Ngrok 응답에 터널이 없습니다.");

        } catch (Exception e) {
            System.setProperty("My_host", DEFAULT_HOST);
            System.err.println("Ngrok URL을 가져오는 데 실패했습니다. 기본 값(" + DEFAULT_HOST + ")을 사용합니다. 오류: " + e.getMessage());
        }
    }
}