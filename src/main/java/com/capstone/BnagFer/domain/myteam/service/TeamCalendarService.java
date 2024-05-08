package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamCalendarRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamCalendarResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
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
    private AccountsServiceUtils accountsServiceUtils;

    public TeamCalendarResponseDto createMatchEvent(TeamCalendarRequestDto request) {
        User user = accountsServiceUtils.getCurrentUser();
        Team team1 = teamRepository.findById(request.team1().getId()).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        Team team2 = teamRepository.findById(request.team2().getId()).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));




    }

}
