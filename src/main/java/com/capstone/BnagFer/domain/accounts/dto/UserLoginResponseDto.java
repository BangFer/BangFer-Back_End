package com.capstone.BnagFer.domain.accounts.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserLoginResponseDto(
        Long userId,
        LocalDateTime createdAt,
        String accessToken,
        String refreshToken
) {

    public static UserLoginResponseDto from(User user, String accessToken, String refreshToken) {
        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
