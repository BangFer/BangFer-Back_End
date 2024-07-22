package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.myteam.dto.request.CUTeamRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.CUTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Role;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.domain.tactic.repository.TacticRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final AccountsCommonService accountsCommonService;
    private final TeamMembersRepository teamMembersRepository;
    private final TacticRepository tacticRepository;

    public CUTeamResponseDto createMyTeam(CUTeamRequestDto request, User user) {

        // 프로필 존재 확인
        accountsCommonService.checkUserProfile(user);

        // 적용 가능 전술 유무 확인
        if (user.getTactics().isEmpty()) {
            throw new TeamExceptionHandler(ErrorCode.USER_TEAM_TACTIC_NOT_FOUND);
        }


        Tactic tactic = tacticRepository.findById(request.tacticId())
                .orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        if (!user.getTactics().contains(tactic)) {
            throw new TacticExceptionHandler(ErrorCode.TACTIC_NOT_ALLOWED);
        }

        Team team = request.toEntity(tactic);
        team.updateLeader(user);
        TeamMember teamMember = TeamMember.createTeamMember(user, Role.LEADER, team);

        teamMembersRepository.save(teamMember);
        teamRepository.save(team);

        return CUTeamResponseDto.from(team);
    }

    public CUTeamResponseDto updateMyTeam(CUTeamRequestDto request, Long teamId, User user) {

        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        if(!team.getLeader().getId().equals(user.getId())) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }

        team.updateTeam(request);

        return CUTeamResponseDto.from(team);
    }

    public void deleteMyTeam(Long teamId, User user) {

        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        if (!team.getLeader().getId().equals(user.getId())) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }

        teamRepository.deleteById(teamId);
    }
}