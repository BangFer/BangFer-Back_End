package com.capstone.BnagFer.domain.myteam.dto.response;

import com.capstone.BnagFer.domain.myteam.entity.CalendarEvent;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TeamCalendarResponseDto  (
        Long id,
        String matchInfo,
        Long teamId,
        LocalDate matchDate
) {
    public static TeamCalendarResponseDto from(CalendarEvent calendarEvent) {
        return TeamCalendarResponseDto.builder()
                .id(calendarEvent.getId())
                .matchInfo(calendarEvent.getMatchInfo())
                .teamId(calendarEvent.getTeam().getId())
                .matchDate(calendarEvent.getMatchDate())
                .build();
    }
}
