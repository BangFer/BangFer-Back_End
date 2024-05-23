package com.capstone.BnagFer.domain.myteam.dto.request;

import com.capstone.BnagFer.domain.myteam.entity.CalendarEvent;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
public record TeamCalendarRequestDto (
        @NotBlank(message = "매치 정보 입력은 필수입니다.")
        String matchInfo,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate matchDate
) {
    public CalendarEvent toEntity(Team team) {
        return CalendarEvent.builder()
                .team(team)
                .matchInfo(matchInfo)
                .matchDate(matchDate)
                .build();
    }


}
