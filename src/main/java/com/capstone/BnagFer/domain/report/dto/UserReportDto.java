package com.capstone.BnagFer.domain.report.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.entity.UserReport;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record UserReportDto(
        User reporter,
        User reportedUser,
        String content,
        LocalDateTime reportedAt

) {
    public static UserReportDto from(UserReport userReport) {
        return UserReportDto.builder()
                .reporter(userReport.getReporter())
                .reportedUser(userReport.getReportedUser())
                .reportedAt(userReport.getReportedAt())
                .build();
    }
    public static UserReport of(User reporter, User reportedUser, String content) {
        return UserReport.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .content(content)
                .build();
    }

}
