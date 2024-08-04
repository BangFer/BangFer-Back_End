package com.capstone.BnagFer.domain.firebase.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.global.util.RedisUtil;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.firebase.dto.FCMAlarmRequestDto;
import com.capstone.BnagFer.domain.firebase.exception.FirebaseExceptionHandler;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FcmAlarmService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserJpaRepository userJpaRepository;
    private final RedisUtil redisUtil;

    public String sendAlarm(FCMAlarmRequestDto requestDto, Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new FirebaseExceptionHandler(ErrorCode.USER_NOT_FOUND));

        String fcmToken = redisUtil.getFCMToken(user.getEmail());
        if (fcmToken != null) {
            Notification notification = Notification.builder()
                    .setTitle(requestDto.title())
                    .setBody(requestDto.body())
                    .build();

            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(notification)
                    .build();

            try {
                firebaseMessaging.send(message);
                return "알림을 성공적으로 전송했습니다. targetUserId = " + userId;
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
                throw new FirebaseExceptionHandler(ErrorCode.FIREBASE_MESSAGING_ERROR);
            }
        } else {
            throw new FirebaseExceptionHandler(ErrorCode.FIREBASE_TOKEN_NOT_FOUND);
        }
    }
}