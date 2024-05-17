package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamCalendarRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.request.UpdateTeamCalendarRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamCalendarResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamCalendarQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamCalendarService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import jakarta.validation.Valid;
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
    public ApiResponse<TeamCalendarResponseDto> createEvent(@PathVariable Long teamId, @RequestBody @Valid TeamCalendarRequestDto request ,
                                                            @LoginUser User user) {
        TeamCalendarResponseDto myEvent = teamCalendarService.createMatchEvent(teamId, request, user);
        return ApiResponse.onSuccess(myEvent);
    }

    @GetMapping("/{teamId}")
    public ApiResponse<List<TeamCalendarResponseDto>> getAllEvents(@PathVariable Long teamId) {
        List<TeamCalendarResponseDto> myEvents = teamCalendarQueryService.getMatchEvents(teamId);
        return ApiResponse.onSuccess(myEvents);
    }

    @GetMapping("/{teamId}/{calendarId}")
    public ApiResponse<TeamCalendarResponseDto> getEvent(@PathVariable Long teamId, @PathVariable Long calendarId) {
        TeamCalendarResponseDto myEvent = teamCalendarQueryService.getOneMatchEvent(teamId, calendarId);
        return ApiResponse.onSuccess(myEvent);
    }


    @PutMapping("/{calendarId}")
    public ApiResponse<TeamCalendarResponseDto> updateEvent(@PathVariable Long calendarId, @RequestBody @Valid UpdateTeamCalendarRequestDto request,
                                                            @LoginUser User user) {
        TeamCalendarResponseDto myEvent = teamCalendarService.updateMatchEvent(request, calendarId, user);
        return ApiResponse.onSuccess(myEvent);
    }

    @DeleteMapping("/{calendarId}")
    public ApiResponse<Void> deleteEvent(@PathVariable Long calendarId, @LoginUser User user) {
        teamCalendarService.deleteMatchEvent(calendarId, user);
        return ApiResponse.noContent();
    }

}
