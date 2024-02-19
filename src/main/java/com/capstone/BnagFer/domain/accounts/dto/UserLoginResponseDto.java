package com.capstone.BnagFer.domain.accounts.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserLoginResponseDto(
        Long userId,
        LocalDateTime createdAt,
        String token
) {

    public static UserLoginResponseDto from(User user, String token) {
        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .createdAt(user.getCreatedAt())
                .token(token)
                .build();
    }
}
