package com.capstone.BnagFer.domain.notification.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.notification.entity.FcmNotification;

public record FcmNotificationRequestDto(
        String title,
        String body
) {
    public FcmNotification toEntity(User user) {
        return FcmNotification.builder()
                .user(user)
                .title(this.title)
                .content(this.body)
                .build();
    }
}