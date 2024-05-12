package com.capstone.BnagFer.domain.accounts.service.profile;

import com.capstone.BnagFer.domain.accounts.dto.profile.CreateProfileRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.ProfileResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.UpdateProfileRequestDto;
import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.ProfileExceptionHandler;
import com.capstone.BnagFer.domain.accounts.repository.ProfileJpaRepository;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProfileService {

    private final ProfileJpaRepository profileJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public ProfileResponseDto createProfile(CreateProfileRequestDto requestDto, User user) {
        Profile profile = requestDto.toEntity(user);
        // 닉네임 이미 존재
        if (profileJpaRepository.existsByNickname(requestDto.nickname())) {
            throw new ProfileExceptionHandler(ErrorCode.NICKNAME_ALREADY_EXIST);
        }

        // 프로필 이미 존재
        if (profileJpaRepository.existsByUser(user)) {
            throw new ProfileExceptionHandler(ErrorCode.PROFILE_ALREADY_EXIST);
        }

        return ProfileResponseDto.from(profileJpaRepository.save(profile), user);
    }

    public ProfileResponseDto updateProfile(Long profileId, UpdateProfileRequestDto requestDto, User user) {
        Profile profile = profileJpaRepository.findById(profileId)
                .orElseThrow(() -> new ProfileExceptionHandler(ErrorCode.PROFILE_NOT_FOUND));

        if (!profile.getUser().getId().equals(user.getId())) {
            throw new ProfileExceptionHandler(ErrorCode.PROFILE_AND_USER_NOT_MATCHED);
        }

        // 이메일이 변경된 경우에만 중복 검사 수행
        if (!requestDto.email().equals(user.getEmail())) {
            if (userJpaRepository.existsByEmail(requestDto.email())) {
                throw new ProfileExceptionHandler(ErrorCode.EMAIL_ALREADY_EXIST);
            }
        }

        if (profileJpaRepository.existsByNickname(requestDto.nickname())) {
            throw new ProfileExceptionHandler(ErrorCode.NICKNAME_ALREADY_EXIST);
        }

        user.updateUser(requestDto);
        profile.updateProfile(requestDto);

        return ProfileResponseDto.from(profileJpaRepository.save(profile), userJpaRepository.save(user));
    }
}
