package com.capstone.BnagFer.domain.report.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;

public record UserRequestDto(
        Long userId,
        Boolean isStaff
) {
    public User toEntity() {
        return User.builder()
                .id(userId)
                .isStaff(isStaff)
                .build();
    }
}
