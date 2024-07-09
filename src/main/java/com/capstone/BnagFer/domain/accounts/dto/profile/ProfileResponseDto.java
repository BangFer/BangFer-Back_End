package com.capstone.BnagFer.domain.accounts.dto.profile;

import com.capstone.BnagFer.domain.accounts.entity.Gender;
import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProfileResponseDto(
        Long userId,
        Long profileId,
        String name,
        String nickName,
        String email,
        String description,
        LocalDate dateOfBirth,
        Gender gender,
        String ProfileImageUrl
) {
    public static ProfileResponseDto from(Profile profile, User user) {
        return ProfileResponseDto.builder()
                .userId(user.getId())
                .profileId(profile.getId())
                .name(user.getName())
                .nickName(profile.getNickname())
                .email(user.getEmail())
                .description(profile.getDescription())
                .description(profile.getDescription())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .ProfileImageUrl(profile.getProfileImageUrl())
                .build();
    }
}
