package com.capstone.BnagFer.domain.firebase.controller;

import com.capstone.BnagFer.domain.firebase.dto.FCMAlarmRequestDto;
import com.capstone.BnagFer.domain.firebase.service.FcmAlarmService;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "푸시 알림 API")
@RequestMapping("/fcm")
public class FcmController {

    private final FcmAlarmService fcmAlarmService;

    @Operation(summary = "푸시 알림 보내기", description = "해당 유저에게 FCM 푸시 알림 전송")
    @PostMapping("/send/{userId}")
    public ApiResponse<String> sendAlarm(@Valid @RequestBody FCMAlarmRequestDto requestDto, @PathVariable Long userId) {
        return ApiResponse.onSuccess(fcmAlarmService.sendAlarm(requestDto, userId));
    }
}