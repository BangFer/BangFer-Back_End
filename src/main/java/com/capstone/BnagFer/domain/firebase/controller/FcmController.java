package com.capstone.BnagFer.domain.firebase.controller;

import com.capstone.BnagFer.domain.firebase.dto.FCMAlarmRequestDto;
import com.capstone.BnagFer.domain.firebase.service.FcmAlarmService;
import com.capstone.BnagFer.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmController {

    private final FcmAlarmService fcmAlarmService;

    @PostMapping("/send")
    public ApiResponse<String> sendAlarm(@Valid @RequestBody FCMAlarmRequestDto requestDto) {
        return ApiResponse.onSuccess(fcmAlarmService.sendAlarm(requestDto));
    }
}