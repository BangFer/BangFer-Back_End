package com.capstone.BnagFer.domain.accounts.dto.social;

import jakarta.validation.constraints.NotBlank;

public record UserSocialLoginRequestDto(
        @NotBlank(message = "[ERROR] 토큰 입력은 필수 입니다.")
        String accessToken,
        String fcmToken
) {
}
