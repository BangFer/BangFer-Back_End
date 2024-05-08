package com.capstone.BnagFer.domain.myteam.dto.request;

import com.capstone.BnagFer.domain.myteam.entity.CalendarEvent;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import javax.net.ssl.SSLSession;
import java.time.LocalDate;
import java.time.LocalTime;
public record TeamCalendarRequestDto (
        Long calendarId,
        @NotBlank(message = "매치 제목 입력은 필수입니다.")
        String matchTitle,
        @NotBlank(message = "매치 설명 입력은 필수입니다.")
        String matchDescription,
        @NotNull(message = "매치 날짜 입력은 필수입니다.")
        LocalDate matchDate,
        @NotNull(message = "매치 시간 입력은 필수입니다.")
        LocalTime matchTime,

        Team team1,
        Team team2
) {
    public CalendarEvent toEntity(Team team) {
        return CalendarEvent.builder()
                .team(team1)
                .team(team2)
                .id(calendarId)
                .matchTitle(matchTitle)
                .matchDescription(matchDescription)
                .matchDate(matchDate)
                .matchTime(matchTime)
                .build();
    }


}
