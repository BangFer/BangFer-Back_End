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
import com.capstone.BnagFer.global.util.s3.S3Provider;
import com.capstone.BnagFer.global.util.s3.dto.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class ProfileService {

    private final ProfileJpaRepository profileJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final S3Provider s3Provider;

    public ProfileResponseDto createProfile(CreateProfileRequestDto requestDto, User user, MultipartFile profileImage) {

        Profile profile = requestDto.toEntity(user);

        uploadProfile(user, profileImage, profile);

        if (profileJpaRepository.existsByNickname(requestDto.nickname())) {
            throw new ProfileExceptionHandler(ErrorCode.NICKNAME_ALREADY_EXIST);
        }

        // 프로필 이미 존재
        if (profileJpaRepository.existsByUser(user)) {
            throw new ProfileExceptionHandler(ErrorCode.PROFILE_ALREADY_EXIST);
        }

        return ProfileResponseDto.from(profileJpaRepository.save(profile), user);
    }

    public ProfileResponseDto updateProfile(Long profileId, UpdateProfileRequestDto requestDto, User user, MultipartFile profileImage) {
        Profile profile = profileJpaRepository.findById(profileId)
                .orElseThrow(() -> new ProfileExceptionHandler(ErrorCode.PROFILE_NOT_FOUND));

        if (!profile.getUser().getId().equals(user.getId())) {
            throw new ProfileExceptionHandler(ErrorCode.PROFILE_AND_USER_NOT_MATCHED);
        }

        uploadProfile(user, profileImage, profile);

        // 나 아닌 다른 사람의 닉네임과 중복된 경우만 에러처리
        Profile existingNickname = profileJpaRepository.findByNickname(requestDto.nickname());
        if (existingNickname != null && !existingNickname.getUser().getId().equals(user.getId())) {
            throw new ProfileExceptionHandler(ErrorCode.NICKNAME_ALREADY_EXIST);
        }

        user.updateUser(requestDto);
        profile.updateProfile(requestDto);

        return ProfileResponseDto.from(profileJpaRepository.save(profile), userJpaRepository.save(user));
    }

    private void uploadProfile(User user, MultipartFile profileImage, Profile profile) {
        if (profileImage != null && !profileImage.isEmpty()) {
            String profileImageUrl = s3Provider.uploadFile(profileImage,
                    S3UploadRequest.builder()
                            .userId(user.getId())
                            .dirName("profile")
                            .build());
            profile.updateProfileImageUrl(profileImageUrl);
        }
    }
}