package com.capstone.BnagFer.domain.accounts.service.profile;

import com.capstone.BnagFer.domain.accounts.dto.profile.OtherUserProfileResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.ProfileResponseDto;
import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.ProfileExceptionHandler;
import com.capstone.BnagFer.domain.accounts.repository.ProfileJpaRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileQueryService {

    private final ProfileJpaRepository profileJpaRepository;

    public ProfileResponseDto getMyProfile(User user) {
        Profile profile = profileJpaRepository.findByUser(user)
                .orElseThrow(() -> new ProfileExceptionHandler(ErrorCode.PROFILE_NOT_FOUND));

        return ProfileResponseDto.from(profile, user);
    }

    public OtherUserProfileResponseDto getOtherUserProfile(Long userId) {
        Profile profile = profileJpaRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileExceptionHandler(ErrorCode.PROFILE_NOT_FOUND));

        return OtherUserProfileResponseDto.from(profile);
    }
}
