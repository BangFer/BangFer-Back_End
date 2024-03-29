package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.myteam.dto.GetTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryService {
    private final TeamRepository teamRepository;

    public GetTeamResponseDto getMyTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        return GetTeamResponseDto.from(team);
    }




}
