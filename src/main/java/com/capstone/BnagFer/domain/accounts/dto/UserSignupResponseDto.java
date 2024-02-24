package com.capstone.BnagFer.domain.accounts.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import lombok.Builder;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Builder
public record UserSignupResponseDto(
        Long id,
        String email,
        String name,
        LocalDateTime createdAt
) {

    public static UserSignupResponseDto from(User user) {
        return UserSignupResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
