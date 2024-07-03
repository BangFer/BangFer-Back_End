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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "프로필 API")
@RequestMapping("/accounts/profile")
@RestController
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileQueryService profileQueryService;

    @Operation(summary = "프로필 생성", description = "닉네임(필수, 중복 불가), 설명, 생일 등을 입력 받아 프로필을 생성함. 프로필은 하나만 생성 가능.")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<ProfileResponseDto> createProfile(@LoginUser User user,
                                                         @Valid @RequestPart("request") CreateProfileRequestDto requestDto,
                                                         @RequestPart(name = "profileImage", required = false) MultipartFile profileImage) {
        return ApiResponse.onSuccess(profileService.createProfile(requestDto, user, profileImage));
    }

    @Operation(summary = "프로필 수정")
    @PutMapping(value = "/{profileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProfileResponseDto> updateProfile(
            @Parameter(description = "프로필 ID", required = true)
            @PathVariable Long profileId,
            @LoginUser User user,
            @Valid @RequestPart("request") UpdateProfileRequestDto requestDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        return ApiResponse.onSuccess(profileService.updateProfile(profileId, requestDto, user, profileImage));
    }

    @Operation(summary = "내 프로필 정보 조회")
    @GetMapping("/myProfile")
    public ApiResponse<ProfileResponseDto> getMyProfile(@LoginUser User user) {
        return ApiResponse.onSuccess(profileQueryService.getMyProfile(user));
    }

    @Operation(summary = "다른 사람 프로필 정보 조회", description = "내 프로필 정보 조회와 다르게 닉네임, 설명, 성별만 조회 가능.")
    @GetMapping("/{userId}")
    public ApiResponse<OtherUserProfileResponseDto> getOtherUserProfile(
            @Parameter(description = "사용자 ID", required = true) @PathVariable Long userId) {
        return ApiResponse.onSuccess(profileQueryService.getOtherUserProfile(userId));
    }
}