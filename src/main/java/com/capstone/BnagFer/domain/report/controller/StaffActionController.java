package com.capstone.BnagFer.domain.report.controller;

import com.capstone.BnagFer.domain.report.dto.UserRequestDto;
import com.capstone.BnagFer.domain.report.dto.UserResponseDto;
import com.capstone.BnagFer.domain.report.dto.UserReportDto;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.service.StaffActionService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffActionController {
    private final StaffActionService staffActionService;

    @GetMapping("/users")
    public ApiResponse<Page<UserResponseDto>> getUserByActivity(@RequestParam UserActivity activity, Pageable pageable) {
        Page<UserResponseDto> userActivityPage = staffActionService.getUserByActivity(activity, pageable);
        return ApiResponse.onSuccess(userActivityPage);
    }

    @GetMapping("/users/{userId}") //해당 유저의 신고 기록 조회
    public ApiResponse<Page<UserReportDto>> getUserReportRecord(@PathVariable Long userId, Pageable pageable) {
        Page<UserReportDto> userReportPage = staffActionService.getUserReportRecord(userId, pageable);
        return ApiResponse.onSuccess(userReportPage);
    }

    @PutMapping("users/{userId}")//유저 활동 변경
    public ApiResponse<UserResponseDto> changeUserActivity(@PathVariable Long userId, @RequestParam UserActivity userActivity) {
        UserResponseDto userDto = staffActionService.changeUserActivity(userActivity, userId);
        return ApiResponse.onSuccess(userDto);
    }

    @PostMapping("/grant-staff") //스태프 권한 부여
    public ApiResponse<UserResponseDto> grantStaffAuthority(@RequestBody UserRequestDto request) {
        // isStaff 값을 true로 설정하여 updateStaffAuthority 호출
        UserResponseDto responseDto = staffActionService.updateStaffAuthority(request, true);
        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping("/revoke-staff") //스태프 권한 해제
    public ApiResponse<UserResponseDto> revokeStaffAuthority(@RequestBody UserRequestDto request) {
        // isStaff 값을 false로 설정하여 updateStaffAuthority 호출
        UserResponseDto responseDto = staffActionService.updateStaffAuthority(request, false);
        return ApiResponse.onSuccess(responseDto);
    }
}
