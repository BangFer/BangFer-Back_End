package com.capstone.BnagFer.domain.accounts.dto.profile;

import com.capstone.BnagFer.domain.accounts.entity.Gender;
import com.capstone.BnagFer.domain.accounts.entity.Profile;
import lombok.Builder;

@Builder
public record OtherUserProfileResponseDto(
        String nickName,
        String description,
        Gender gender,
        String profileImageUrl
) {
    public static OtherUserProfileResponseDto from(Profile profile) {
        return OtherUserProfileResponseDto.builder()
                .nickName(profile.getNickname())
                .description(profile.getDescription())
                .gender(profile.getGender())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }
}
