package com.capstone.BnagFer.domain.accounts.dto.social;

import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;

public record UserSocialSignupRequestDto(
        @NotBlank(message = "[ERROR] 토큰 입력은 필수 입니다.")
        String accessToken,
        @NotBlank(message = "[ERROR] 이메일 입력은 필수 입니다.")
        String email
) {
}
