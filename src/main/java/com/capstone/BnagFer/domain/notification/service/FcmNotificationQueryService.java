package com.capstone.BnagFer.domain.notification.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.notification.dto.NotificationResponseDto;
import com.capstone.BnagFer.domain.notification.entity.FcmNotification;
import com.capstone.BnagFer.domain.notification.repository.FcmNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FcmNotificationQueryService {

    private final FcmNotificationRepository fcmNotificationRepository;

    public List<NotificationResponseDto> getUserNotifications(User user) {

        List<FcmNotification> notifications = fcmNotificationRepository.findRecentByUserId(user.getId());

        return notifications.stream()
                .map(NotificationResponseDto::from)
                .collect(Collectors.toList());
    }
}
