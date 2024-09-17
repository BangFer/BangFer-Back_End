package com.capstone.BnagFer.domain.notification.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.notification.entity.FcmNotification;
import com.capstone.BnagFer.domain.notification.repository.FcmNotificationRepository;
import com.capstone.BnagFer.global.util.RedisUtil;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.exception.FcmNotificationExceptionHandler;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class FcmNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserJpaRepository userJpaRepository;
    private final RedisUtil redisUtil;
    private final FcmNotificationRepository fcmNotificationRepository;

    // 항상 새로운 트랜잭션에서 실행
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FcmNotification saveNotification(FcmNotificationRequestDto requestDto, User user) {
        FcmNotification fcmNotification = requestDto.toEntity(user);
        return fcmNotificationRepository.save(fcmNotification);
    }

    // 트랜잭션 없이 실행
    // 알림 저장과 FCM 전송을 분리하여, FCM 전송 실패 시에도 알림이 저장되도록
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String sendAlarm(FcmNotificationRequestDto requestDto, Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new FcmNotificationExceptionHandler(ErrorCode.USER_NOT_FOUND));

        // 알림 저장 (별도의 트랜잭션)
        saveNotification(requestDto, user);

        String fcmToken = redisUtil.getFCMToken(user.getEmail());
        if (fcmToken == null) {
            throw new FcmNotificationExceptionHandler(ErrorCode.FIREBASE_TOKEN_NOT_FOUND);
        }

        try {
            sendFcmNotification(requestDto, fcmToken);
            return "알림을 성공적으로 전송했습니다. targetUserId = " + userId;
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            throw new FcmNotificationExceptionHandler(ErrorCode.FIREBASE_MESSAGING_ERROR);
        }
    }

    private void sendFcmNotification(FcmNotificationRequestDto requestDto, String fcmToken) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(requestDto.title())
                .setBody(requestDto.body())
                .build();

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .build();

        firebaseMessaging.send(message);
    }
}