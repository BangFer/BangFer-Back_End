package com.capstone.BnagFer.domain.report.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.dto.UserReportRequest;
import com.capstone.BnagFer.domain.report.service.ReportService;
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
    public ApiResponse<Void> repostUser(@RequestBody UserReportRequest userReportRequest,
                                        User user, @PathVariable Long reportedUserId) {

        reportService.reportUser(user.getId(), reportedUserId, userReportRequest.content());
        return ApiResponse.noContent();
    }
}
