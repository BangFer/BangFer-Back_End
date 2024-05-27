package com.capstone.BnagFer.domain.accounts.controller;

import com.capstone.BnagFer.domain.accounts.dto.profile.CreateProfileRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.OtherUserProfileResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.ProfileResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.UpdateProfileRequestDto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.profile.ProfileQueryService;
import com.capstone.BnagFer.domain.accounts.service.profile.ProfileService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "프로필 API")
@RequestMapping("/accounts/profile")
@RestController
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileQueryService profileQueryService;

    @Operation(summary = "프로필 생성", description = "닉네임(필수, 중복 불가), 설명, 생일 등을 입력 받아 프로필을 생성함. 프로필은 하나만 생성 가능.")
    @PostMapping
    public ApiResponse<ProfileResponseDto> createProfile(@Valid @RequestBody CreateProfileRequestDto requestDto,
                                                         @LoginUser User user) {
        return ApiResponse.onSuccess(profileService.createProfile(requestDto, user));
    }

    @Operation(summary = "프로필 수정")
    @PutMapping("/{profileId}")
    public ApiResponse<ProfileResponseDto> updateProfile(
            @PathVariable Long profileId,
            @LoginUser User user,
            @Valid @RequestBody UpdateProfileRequestDto requestDto) {
        return ApiResponse.onSuccess(profileService.updateProfile(profileId, requestDto, user));
    }

    @Operation(summary = "내 프로필 정보 조회")
    @GetMapping("/myProfile")
    public ApiResponse<ProfileResponseDto> getMyProfile(@LoginUser User user) {
        return ApiResponse.onSuccess(profileQueryService.getMyProfile(user));
    }

    @Operation(summary = "다른 사람 프로필 정보 조회", description = "내 프로필 정보 조회와 다르게 닉네임, 설명, 성별만 조회 가능.")
    @GetMapping("/{userId}")
    public ApiResponse<OtherUserProfileResponseDto> getOtherUserProfile(@PathVariable Long userId) {
        return ApiResponse.onSuccess(profileQueryService.getOtherUserProfile(userId));
    }
}
