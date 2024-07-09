package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamCalendarRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamCalendarResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamCalendarQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamCalendarService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "팀 캘린더 API")
@RequestMapping("/team/calendar")
public class TeamCalendarController {

    private final TeamCalendarService teamCalendarService;
    private final TeamCalendarQueryService teamCalendarQueryService;

    @Operation(summary = "캘린더 이벤트 생성", description = "매치 날짜로 등록.")
    @PostMapping("/{teamId}")
    public ApiResponse<TeamCalendarResponseDto> createEvent(@PathVariable(name = "teamId") Long teamId,
                                                            @RequestBody @Valid TeamCalendarRequestDto request ,
                                                            @LoginUser User user) {
        TeamCalendarResponseDto myEvent = teamCalendarService.createMatchEvent(teamId, request, user);
        return ApiResponse.onSuccess(myEvent);
    }

    @Operation(summary = "팀 전체 캘린더 이벤트")
    @GetMapping("/{teamId}")
    public ApiResponse<List<TeamCalendarResponseDto>> getAllEvents(@PathVariable(name = "teamId") Long teamId) {
        List<TeamCalendarResponseDto> myEvents = teamCalendarQueryService.getMatchEvents(teamId);
        return ApiResponse.onSuccess(myEvents);
    }

    @Operation(summary = "팀의 개별 이벤트 조회")
    @GetMapping("/{teamId}/{calendarId}")
    public ApiResponse<TeamCalendarResponseDto> getEvent(@PathVariable(name = "teamId") Long teamId,
                                                         @PathVariable(name = "calendarId") Long calendarId) {
        TeamCalendarResponseDto myEvent = teamCalendarQueryService.getOneMatchEvent(teamId, calendarId);
        return ApiResponse.onSuccess(myEvent);
    }

    @Operation(summary = "캘린더 이벤트 삭제")
    @DeleteMapping("/{calendarId}")
    public ApiResponse<Void> deleteEvent(@PathVariable(name = "calendarId") Long calendarId, @LoginUser User user) {
        teamCalendarService.deleteMatchEvent(calendarId, user);
        return ApiResponse.noContent();
    }
}
