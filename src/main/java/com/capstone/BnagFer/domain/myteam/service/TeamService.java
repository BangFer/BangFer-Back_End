
package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.myteam.dto.request.CUTeamRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.CUTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
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
    private final AccountsCommonService accountsCommonService;
    private final TeamMembersRepository teamMembersRepository;

    public CUTeamResponseDto createMyTeam(CUTeamRequestDto request, User user) {
        Team team = request.toEntity();
        team.updateLeader(user);
        TeamMember teamMember = TeamMember.createTeamMember();

        // 프로필 존재 확인
        accountsCommonService.checkUserProfile(team.getLeader());

        teamMember.updateUserRoleAndTeam(user, team);
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
        Team updatedTeam = teamRepository.save(team);
        return CUTeamResponseDto.from(updatedTeam);
    }

    public void deleteMyTeam(Long teamId, User user) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        if (!team.getLeader().getId().equals(user.getId())) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        teamRepository.deleteById(teamId);
    }
}