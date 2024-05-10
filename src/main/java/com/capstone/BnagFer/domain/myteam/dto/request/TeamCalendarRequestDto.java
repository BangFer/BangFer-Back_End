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
        @NotBlank(message = "매치 정보 입력은 필수입니다.")
        String matchInfo,
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
