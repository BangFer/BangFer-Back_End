package com.capstone.BnagFer.domain.report.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.entity.UserReport;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record UserReportResponseDto(
        Long reporter,
        Long reportedUser,
        String content,
        LocalDateTime reportedAt

) {
    public static UserReportResponseDto from(UserReport userReport) {
        return UserReportResponseDto.builder()
                .reporter(userReport.getReporter().getId())
                .reportedUser(userReport.getReportedUser().getId())
                .content(userReport.getContent())
                .reportedAt(userReport.getReportedAt())
                .build();
    }
    public static UserReportResponseDto of(User reporter, User reportedUser, String content) {
        return UserReportResponseDto.builder()
                .reporter(reporter.getId())
                .reportedUser(reportedUser.getId())
                .content(content)
                .build();
    }

}
