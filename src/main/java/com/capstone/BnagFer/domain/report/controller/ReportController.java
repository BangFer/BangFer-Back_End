package com.capstone.BnagFer.domain.report.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.dto.UserReportRequest;
import com.capstone.BnagFer.domain.report.dto.UserReportResponseDto;
import com.capstone.BnagFer.domain.report.service.ReportService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController{
    private final ReportService reportService;
    //사용자 신고
    @PostMapping("user/{reportedUserId}")
    public ApiResponse<UserReportResponseDto> reportUser(@RequestBody UserReportRequest userReportRequest, @PathVariable Long reportedUserId, @LoginUser User user) {
        UserReportResponseDto reportResponseDto = reportService.reportUser(userReportRequest, user, reportedUserId);
        return ApiResponse.onSuccess(reportResponseDto);
    }
}
