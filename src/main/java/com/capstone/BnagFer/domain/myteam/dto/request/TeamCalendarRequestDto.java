package com.capstone.BnagFer.domain.myteam.dto.request;

import com.capstone.BnagFer.domain.myteam.entity.CalendarEvent;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
public record TeamCalendarRequestDto (
        @NotNull(message = "[ERROR] 날짜는 필수입니다.")
        @Schema(description = "이벤트 날짜", example = "2024-06-02")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate matchDate
) {
    public CalendarEvent toEntity(Team team) {
        return CalendarEvent.builder()
                .team(team)
                .matchDate(matchDate)
                .build();
    }


}
