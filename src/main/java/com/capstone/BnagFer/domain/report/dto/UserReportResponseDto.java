package com.capstone.BnagFer.domain.report.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.entity.UserReport;
import lombok.Builder;

@Builder
public record UserReportResponseDto(
        User reporter,
        User reportedUser,
        String content
) {
    public static UserReport of(User reporter, User reportedUser, String content) {
        return UserReport.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .content(content)
                .build();
    }
}
