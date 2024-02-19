package com.capstone.BnagFer.domain.accounts.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import lombok.Builder;

@Builder
public record UserSignupResponseDto(
        Long id,
        String email,
        String name
) {

    public static UserSignupResponseDto from(User user) {
        return UserSignupResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
