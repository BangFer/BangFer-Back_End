package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamCalendarRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.request.UpdateTeamCalendarRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamCalendarResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.CalendarEvent;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.exception.EventExceptionHandler;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamCalendarRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamCalendarService {
    private final TeamCalendarRepository teamCalendarRepository;
    private final TeamRepository teamRepository;

    public TeamCalendarResponseDto createMatchEvent(Long teamId, TeamCalendarRequestDto request, User user) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        if(user.getId().equals(team.getLeader().getId())) {
            CalendarEvent calendarEvent = request.toEntity(team);
            teamCalendarRepository.save(calendarEvent);
            return TeamCalendarResponseDto.from(calendarEvent);
        }
        else {
            throw new AccountsExceptionHandler(ErrorCode.NO_AUTHORIZATION);
        }
    }


    public TeamCalendarResponseDto updateMatchEvent(UpdateTeamCalendarRequestDto request, Long calendarId, User user) {

        CalendarEvent event = teamCalendarRepository.findById(calendarId).orElseThrow(() -> new EventExceptionHandler(ErrorCode.MATCH_EVENT_NOT_EXIST));
        if(user.getId().equals(event.getTeam().getLeader().getId())) {
            event.updateMatchInfo(request);
            teamCalendarRepository.save(event);
            return TeamCalendarResponseDto.from(event);
        }
        else {
            throw new AccountsExceptionHandler(ErrorCode.NO_AUTHORIZATION);
        }
    }

    public void deleteMatchEvent(Long calendarId, User user) {
        CalendarEvent event = teamCalendarRepository.findById(calendarId).orElseThrow(() -> new EventExceptionHandler(ErrorCode.MATCH_EVENT_NOT_EXIST));
        if(user.getId().equals(event.getTeam().getLeader().getId())) {
            teamCalendarRepository.deleteById(calendarId);
        }
        else {
            throw new AccountsExceptionHandler(ErrorCode.NO_AUTHORIZATION);
        }
    }
}
