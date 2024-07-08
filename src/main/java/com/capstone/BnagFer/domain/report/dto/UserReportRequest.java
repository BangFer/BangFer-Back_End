package com.capstone.BnagFer.domain.report.dto;
import com.capstone.BnagFer.domain.report.entity.UserReport;
public record UserReportRequest(String content) {
    public UserReport toEntity(){
        return UserReport.builder()
                .content(content)
                .build();
    }
}
