package com.capstone.BnagFer.domain.report.controller;

import com.capstone.BnagFer.domain.report.dto.UserDto;
import com.capstone.BnagFer.domain.report.dto.UserReportDto;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.service.ReportActionService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ReportActionController {
    private final ReportActionService reportActionService;
    @GetMapping("/users")
    public ApiResponse<Page<UserDto>> getUserByActivity(@RequestParam UserActivity activity, Pageable pageable) {
        Page<UserDto> userActivityPage = reportActionService.getUserByActivity(activity, pageable);
        return ApiResponse.onSuccess(userActivityPage);
    }

    @GetMapping("/users/{userId}") //해당 유저의 신고 기록 조회
    public ApiResponse<Page<UserReportDto>> getUserReportRecord(@PathVariable Long userId, Pageable pageable) {
        Page<UserReportDto> userReportPage = reportActionService.getUserReportRecord(userId, pageable);
        return ApiResponse.onSuccess(userReportPage);
    }

    @PutMapping("users/{userId}")
    public ApiResponse<UserDto> changeUserActivity(@PathVariable Long userId, @RequestParam UserActivity userActivity) {
        UserDto userDto = reportActionService.changeUserActivity(userActivity, userId);
        return ApiResponse.onSuccess(userDto);
    }
}
