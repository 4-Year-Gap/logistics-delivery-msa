package com.springcloud.management.interfaces.external;

import com.springcloud.management.application.dto.ListSlackQuery;
import com.springcloud.management.application.service.SlackService;
import com.springcloud.management.domain.service.UserRole;
import com.springcloud.management.interfaces.dto.*;
import com.springcloud.management.interfaces.exception.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public ResponseEntity<ResponseDto<ListSlackQuery>> searchSlackMessage(
            @ModelAttribute SearchSlackRequest requestDto,
            Pageable pageable,
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestHeader("X-USER-ROLE") UserRole role) {

        ListSlackQuery listSlackQuery = slackService.searchSlackMessage(requestDto, pageable, userId, role);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(listSlackQuery));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto<UpdateSlackResponse>> updateSlackMessage(
            @RequestBody UpdateSlackRequest requestDto,
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestHeader("X-USER-ROLE") UserRole role) {

        UpdateSlackResponse updateSlackResponse = slackService.updateSlackMessage(requestDto, userId, role);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(updateSlackResponse));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<DeleteSlackResponse>> deleteSlackMessage(
            @RequestBody DeleteSlackRequest requestDto,
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestHeader("X-USER-ROLE") UserRole role) {

        DeleteSlackResponse deleteSlackResponse = slackService.deleteSlackMessage(requestDto, userId, role);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(deleteSlackResponse));
    }
}
