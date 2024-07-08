package com.capstone.BnagFer.domain.report.dto;
import com.capstone.BnagFer.domain.report.entity.UserReport;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record UserReportResponseDto(
        Long id,
        Long reporter,
        Long reportedUser,
        String content,
        LocalDateTime reportedAt

) {
    public static UserReportResponseDto from(UserReport userReport) {
        return UserReportResponseDto.builder()
                .id(userReport.getId())
                .reporter(userReport.getReporter().getId())
                .reportedUser(userReport.getReportedUser().getId())
                .content(userReport.getContent())
                .reportedAt(userReport.getReportedAt())
                .build();
    }
}
