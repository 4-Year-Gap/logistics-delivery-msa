package com.springcloud.management.interfaces.external;

import com.springcloud.management.application.service.SlackService;
import com.springcloud.management.interfaces.dto.CreateSlackRequest;
import com.springcloud.management.interfaces.dto.CreateSlackResponse;
import com.springcloud.management.interfaces.exception.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slack")
public class SlackExternalController {

    private final SlackService slackService;

    @PostMapping("/message/{userId}")
    public ResponseEntity<ResponseDto<CreateSlackResponse>> saveSlackMessage(@RequestBody CreateSlackRequest requestDto,
                                                                             @PathVariable UUID userId) {
        CreateSlackResponse createSlackResponse = slackService.saveSlackMessage(requestDto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(createSlackResponse));
    }
}
