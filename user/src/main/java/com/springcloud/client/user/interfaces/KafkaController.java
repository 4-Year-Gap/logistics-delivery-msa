package com.springcloud.client.user.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/send/test")
    public String sendMessage(@RequestParam("topic") String topic, @RequestBody IdentityIntegrationDto request) throws JsonProcessingException {
        // DTO -> JSON 변환
        String jsonMessage = objectMapper.writeValueAsString(request);

        String key = request.getDomain() + ":" + request.getEventType();

        // Kafka 전송
        kafkaTemplate.send(topic, key, jsonMessage);

        return "Message sent to Kafka topic";
    }
}