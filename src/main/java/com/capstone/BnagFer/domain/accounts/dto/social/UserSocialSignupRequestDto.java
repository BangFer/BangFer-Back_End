package com.capstone.BnagFer.domain.accounts.dto.social;

import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public record UserSocialSignupRequestDto(
        String accessToken,
        String email
) {
}
