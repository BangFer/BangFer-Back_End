package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {
    private final TeamRepository teamRepository;
    private final AccountsServiceUtils accountsServiceUtils;

    public CUTeamResponseDto createMyTeam(CUTeamRequestDto request) {
        User user = accountsServiceUtils.getCurrentUser();
        Team team = request.toEntity();
        team.setLeader(user);
        teamRepository.save(team);
        return CUTeamResponseDto.from(team);
    }

    public CUTeamResponseDto updateMyTeam(CUTeamRequestDto request) {
        User user = accountsServiceUtils.getCurrentUser();
        Team team = teamRepository.findById(user.getId()).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        team.updateTeam(request);
        Team updatedTeam = teamRepository.save(team);
        return CUTeamResponseDto.from(updatedTeam);
    }

    public void deleteMyTeam(Long teamId) { teamRepository.deleteById(teamId); }
}
