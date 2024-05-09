package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.myteam.dto.request.TeamCalendarRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.request.UpdateTeamCalendarRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamCalendarResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamCalendarQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamCalendarService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/calendar")
public class TeamCalendarController {
    private final TeamCalendarService teamCalendarService;
    private final TeamCalendarQueryService teamCalendarQueryService;

    @PostMapping("/{teamId}")
    public ApiResponse<TeamCalendarResponseDto> createEvent(@PathVariable Long teamId, @RequestBody TeamCalendarRequestDto request) {
        TeamCalendarResponseDto myEvent = teamCalendarService.createMatchEvent(teamId, request);
        return ApiResponse.onSuccess(myEvent);
    }

    @GetMapping("/all/{teamId}")
    public ApiResponse<List<TeamCalendarResponseDto>> getAllEvents(@PathVariable Long teamId) {
        List<TeamCalendarResponseDto> myEvents = teamCalendarQueryService.getMatchEvents(teamId);
        return ApiResponse.onSuccess(myEvents);
    }

    @GetMapping("/{teamId}")
    public ApiResponse<TeamCalendarResponseDto> getEvent(@PathVariable Long teamId) {
        TeamCalendarResponseDto myEvent = teamCalendarQueryService.getOneMatchEvent(teamId);
        return ApiResponse.onSuccess(myEvent);
    }


    @PutMapping("/{calendarId}")
    public ApiResponse<TeamCalendarResponseDto> updateEvent(@PathVariable Long calendarId, @RequestBody UpdateTeamCalendarRequestDto request) {
        TeamCalendarResponseDto myEvent = teamCalendarService.updateMatchEvent(request, calendarId);
        return ApiResponse.onSuccess(myEvent);
    }

    @DeleteMapping("/{calendarId}")
    public ApiResponse<Void> deleteEvent(@PathVariable Long calendarId) {
        teamCalendarService.deleteMatchEvent(calendarId);
        return ApiResponse.noContent();
    }

}
