package com.capstone.BnagFer.domain.myteam.dto.response;

import com.capstone.BnagFer.domain.myteam.entity.CalendarEvent;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record TeamCalendarResponseDto  (
        Long id,
        String matchTitle,
        String matchDescription,
        String teamName1,
        String teamName2,
        LocalDate matchDate,
        LocalTime matchTime
) {
    public static TeamCalendarResponseDto from(CalendarEvent calendarEvent, Team team) {
        return TeamCalendarResponseDto.builder()
                .id(calendarEvent.getId())
                .matchTitle(calendarEvent.getMatchTitle())
                .matchDescription(calendarEvent.getMatchDescription())
                .teamName1(team.getTeamName())
                .teamName2(team.getTeamName())
                .matchDate(calendarEvent.getMatchtDate())
                .matchTime(calendarEvent.getMatchTime())
                .build();
    }
}
