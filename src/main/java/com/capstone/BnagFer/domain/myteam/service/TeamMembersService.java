package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.myteam.dto.TeamMemberRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.TeamMembersResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Role;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.exception.TeamMemberExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional
public class TeamMembersService {
    private final AccountsServiceUtils accountsServiceUtils;
    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;
    private final UserJpaRepository userJpaRepository;

    public TeamMembersResponseDto addTeamMembers(TeamMemberRequestDto request) {
//        User user = userJpaRepository.findById(accountsServiceUtils.getCurrentUser().getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Team team = teamRepository.findById(request.teamId()).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        User invitedUser = userJpaRepository.findById(request.userId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (team.getLeader().getId() == invitedUser.getId()) {
            throw new TeamExceptionHandler(ErrorCode._BAD_REQUEST);
        }
        TeamMember existingMember = teamMembersRepository.findByTeamAndUser(team, invitedUser);
        if (existingMember != null) {
            // 이미 초대된 사용자이므로 예외 처리
            throw new TeamMemberExceptionHandler(ErrorCode.TEAMMEMBER_EXISTS);
        }
        TeamMember teamMember = request.toEntity(invitedUser, team);
        teamMember.setRole(Role.MEMBER);
        teamMember.setUser(invitedUser);
            teamMembersRepository.save(teamMember);
        return TeamMembersResponseDto.from(teamMember);
    }
}
