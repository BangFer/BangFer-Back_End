package com.capstone.BnagFer.domain.report.controller;

import com.capstone.BnagFer.domain.report.dto.UserRequestDto;
import com.capstone.BnagFer.domain.report.dto.UserResponseDto;
import com.capstone.BnagFer.domain.report.dto.UserReportResponseDto;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.service.StaffActionQueryService;
import com.capstone.BnagFer.domain.report.service.StaffActionService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffActionController {
    private final StaffActionService staffActionService;
    private final StaffActionQueryService staffActionQueryService;

    @GetMapping("/users")
    public ApiResponse<List<UserResponseDto>> getUserByActivity(@RequestParam UserActivity activity) {
        List<UserResponseDto> userActivityPage = staffActionQueryService.getUserByActivity(activity);
        return ApiResponse.onSuccess(userActivityPage);
    }

    @GetMapping("/users/{userId}") //해당 유저의 신고 기록 조회
    public ApiResponse<List<UserReportResponseDto>> getUserReportRecord(@PathVariable Long userId) {
        List<UserReportResponseDto> userReportPage = staffActionQueryService.getUserReportRecord(userId);
        return ApiResponse.onSuccess(userReportPage);
    }

    @PutMapping("users/{userId}")//유저 활동 변경
    public ApiResponse<UserResponseDto> changeUserActivity(@PathVariable Long userId, @RequestParam UserActivity userActivity) {
        UserResponseDto userDto = staffActionService.changeUserActivity(userActivity, userId);
        return ApiResponse.onSuccess(userDto);
    }

//    @PostMapping("/grant-staff") //스태프 권한 부여
//    public ApiResponse<UserResponseDto> grantStaffAuthority(@RequestBody UserRequestDto request) {
//        // isStaff 값을 true로 설정하여 updateStaffAuthority 호출
//        UserResponseDto responseDto = staffActionService.updateStaffAuthority(request, true);
//        return ApiResponse.onSuccess(responseDto);
//    }
@PostMapping("/grant-staff/{userId}") //스태프 권한 부여
public ApiResponse<UserResponseDto> grantStaffAuthority(@PathVariable Long userId) {
    // isStaff 값을 true로 설정하여 updateStaffAuthority 호출
    UserResponseDto responseDto = staffActionService.grantStaffAuthority(userId);
    return ApiResponse.onSuccess(responseDto);
}

//    @PostMapping("/revoke-staff") //스태프 권한 해제
//    public ApiResponse<UserResponseDto> revokeStaffAuthority(@RequestBody UserRequestDto request) {
//        // isStaff 값을 false로 설정하여 updateStaffAuthority 호출
//        UserResponseDto responseDto = staffActionService.updateStaffAuthority(request, false);
//        return ApiResponse.onSuccess(responseDto);
//    }
@PostMapping("/revoke-staff/{userId}") //스태프 권한 해제
public ApiResponse<UserResponseDto> revokeStaffAuthority(@PathVariable Long userId) {
    // isStaff 값을 false로 설정하여 updateStaffAuthority 호출
    UserResponseDto responseDto = staffActionService.revokeStaffAuthority(userId);
    return ApiResponse.onSuccess(responseDto);
}
}
