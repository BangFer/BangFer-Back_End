package com.capstone.BnagFer.domain.accounts.controller;

import com.capstone.BnagFer.domain.accounts.dto.profile.CreateProfileRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.OtherUserProfileResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.ProfileResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.UpdateProfileRequestDto;
import com.capstone.BnagFer.domain.accounts.service.profile.ProfileQueryService;
import com.capstone.BnagFer.domain.accounts.service.profile.ProfileService;
import com.capstone.BnagFer.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/accounts/profile")
@RestController
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileQueryService profileQueryService;

    @PostMapping
    public ApiResponse<ProfileResponseDto> createProfile(@Valid @RequestBody CreateProfileRequestDto requestDto) {
        return ApiResponse.onSuccess(profileService.createProfile(requestDto));
    }

    @PutMapping("/{profileId}")
    public ApiResponse<ProfileResponseDto> updateProfile(
            @PathVariable Long profileId,
            @Valid @RequestBody UpdateProfileRequestDto requestDto) {
        return ApiResponse.onSuccess(profileService.updateProfile(profileId, requestDto));
    }

    // 내 프로필 조회
    @GetMapping("/myProfile")
    public ApiResponse<ProfileResponseDto> getMyProfile() {
        return ApiResponse.onSuccess(profileQueryService.getMyProfile());
    }

    // 다른 사람 프로필 조회
    @GetMapping("/{userId}")
    public ApiResponse<OtherUserProfileResponseDto> getOtherUserProfile(@PathVariable Long userId) {
        return ApiResponse.onSuccess(profileQueryService.getOtherUserProfile(userId));
    }

}
