package com.capstone.BnagFer.domain.notification.dto;

import com.capstone.BnagFer.domain.notification.entity.FcmNotification;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponseDto(
        String title,
        String body,
        LocalDateTime createdAt
) {
    public static NotificationResponseDto from(FcmNotification notification) {
        return NotificationResponseDto.builder()
                .title(notification.getTitle())
                .body(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
