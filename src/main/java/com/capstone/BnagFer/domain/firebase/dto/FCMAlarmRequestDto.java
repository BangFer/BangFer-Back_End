package com.capstone.BnagFer.domain.firebase.dto;

public record FCMAlarmRequestDto (
        Long targetUserId,
        String title,
        String body
) {
}
