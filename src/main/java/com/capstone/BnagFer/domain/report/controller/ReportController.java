package com.capstone.BnagFer.domain.report.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.dto.UserReportRequest;
import com.capstone.BnagFer.domain.report.dto.UserReportResponseDto;
import com.capstone.BnagFer.domain.report.service.ReportService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController{
    private final ReportService reportService;
    @Operation(summary = "사용자 신고", description = "신고를 당하면 해당 사용자의 userActivity값은 NORMAL에서 FLAGGED로 변환된다.")
    @PostMapping("user/{reportedUserId}")
    public ApiResponse<UserReportResponseDto> reportUser(@RequestBody UserReportRequest userReportRequest, @PathVariable Long reportedUserId, @LoginUser User user) {
        UserReportResponseDto reportResponseDto = reportService.reportUser(userReportRequest, user, reportedUserId);
        return ApiResponse.onSuccess(reportResponseDto);
    }
}
