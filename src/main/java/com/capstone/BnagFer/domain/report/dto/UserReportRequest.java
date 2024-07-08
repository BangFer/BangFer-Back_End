package com.capstone.BnagFer.domain.report.dto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.entity.UserReport;
public record UserReportRequest(
        String content
) {
    public UserReport toEntity(User reporter, User reportedUser){
        return UserReport.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .content(content)
                .build();
    }
}
