
package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Role;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {
    private final TeamRepository teamRepository;
    private final AccountsServiceUtils accountsServiceUtils;
    private final TeamMembersRepository teamMembersRepository;

    public CUTeamResponseDto createMyTeam(CUTeamRequestDto request) {
        User user = accountsServiceUtils.getCurrentUser();
        Team team = request.toEntity();
        team.setLeader(user);
        TeamMember teamMember = TeamMember.createTeamMember();
        if (team.getLeader().getProfile()== null)
            throw new AccountsExceptionHandler(ErrorCode.PROFILE_NOT_EXIST);
        teamMember.setTeam(team);
        teamMember.setUser(user);
        teamMember.setRole(Role.LEADER);
        teamMembersRepository.save(teamMember);
        teamRepository.save(team);
        return CUTeamResponseDto.from(team);
    }

    public CUTeamResponseDto updateMyTeam(CUTeamRequestDto request, Long teamId) {
        User user = accountsServiceUtils.getCurrentUser();
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        if(team.getLeader().getId() != user.getId()) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        team.updateTeam(request);
        Team updatedTeam = teamRepository.save(team);
        return CUTeamResponseDto.from(updatedTeam);
    }
    public void deleteMyTeam(Long teamId) {
        User user = accountsServiceUtils.getCurrentUser();
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        if(team.getLeader().getId()!= user.getId()) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        teamRepository.deleteById(teamId); }
}