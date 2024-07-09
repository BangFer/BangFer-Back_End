package com.capstone.BnagFer.domain.report.dto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.entity.ReportActivity;
import com.capstone.BnagFer.domain.report.entity.UserReport;
public record UserReportRequest() {
    public UserReport toEntity(User reporter, User reportedUser, ReportActivity reportActivity){
        return UserReport.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reportActivity(reportActivity)
                .build();
    }
}
