package com.capstone.BnagFer.domain.report.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String name,
        String nickName,
        String email,
        Boolean isStaff,
        UserActivity userActivity

) {
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .nickName(user.getProfile().getNickname())
                .email(user.getEmail())
                .isStaff(user.getIsStaff())
                .userActivity(user.getUserActivity())
                .build();
    }
}
