package com.capstone.BnagFer.domain.notification.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.dto.NotificationResponseDto;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationQueryService;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "푸시 알림 API")
@RequestMapping("/fcm")
public class FcmNotificationController {

    private final FcmNotificationService fcmNotificationService;
    private final FcmNotificationQueryService fcmNotificationQueryService;

    @Operation(summary = "푸시 알림 보내기", description = "테스트 용. 해당 유저에게 FCM 푸시 알림 전송")
    @PostMapping("/send/{userId}")
    public ApiResponse<String> sendAlarm(@Valid @RequestBody FcmNotificationRequestDto requestDto, @PathVariable Long userId) {
        return ApiResponse.onSuccess(fcmNotificationService.sendAlarm(requestDto, userId));
    }

    @Operation(summary = "알림 조회하기", description = "사용자한테 온 알림 조회")
    @PostMapping("/notification")
    public ApiResponse<List<NotificationResponseDto>> getUserNotifications(@LoginUser User user) {
        return ApiResponse.onSuccess(fcmNotificationQueryService.getUserNotifications(user));
    }

}