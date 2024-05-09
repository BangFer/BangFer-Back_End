package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.myteam.dto.request.TeamCalendarRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamCalendarResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMembersResponseDto;
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
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamCalendarQueryService {
    private final TeamRepository teamRepository;
    private final TeamCalendarRepository teamCalendarRepository;
    public List<TeamCalendarResponseDto> getMatchEvents(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        List<CalendarEvent> matchEvents = teamCalendarRepository.findAllByTeam(team);
        return matchEvents.stream()
                .map(TeamCalendarResponseDto::from)
                .toList();
    }

    public TeamCalendarResponseDto getOneMatchEvent(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        CalendarEvent event = teamCalendarRepository.findOneByTeam(team);
        return TeamCalendarResponseDto.from(event);
    }

}
