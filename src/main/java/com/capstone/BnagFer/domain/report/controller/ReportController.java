package com.capstone.BnagFer.domain.report.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.dto.UserReportResponseDto;
import com.capstone.BnagFer.domain.report.entity.ReportActivity;
import com.capstone.BnagFer.domain.report.service.ReportService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "신고 API")
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController{
    private final ReportService reportService;
    @Operation(summary = "사용자 신고",
            description = "신고를 당하면 해당 사용자의 userActivity값은 NORMAL에서 FLAGGED로 변환된다." +
                    " ReportActivity 종류" +
                    " 1. NORMAL: 정상적인 게시물" +
                    " 2. CURSING: 욕설/비하" +
                    " 3. OBSCENE: 음란물/불건전한 만남 및 대화" +
                    " 4. POLITICAL: 정치적 발언" +
                    " 5. IMPOSTOR: 사칭" +
                    " 6. COMMERCIAL: 상업적 광고 및 판매")

    @PostMapping("user/{reportedUserId}")
    public ApiResponse<UserReportResponseDto> reportUser(@RequestParam ReportActivity reportActivity, @PathVariable Long reportedUserId, @LoginUser User user) {
        UserReportResponseDto reportResponseDto = reportService.reportUser(user, reportedUserId, reportActivity);
        return ApiResponse.onSuccess(reportResponseDto);
    }
}
