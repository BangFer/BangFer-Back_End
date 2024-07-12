package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamInviteRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamInviteResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMembersResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.*;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.exception.TeamMemberExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamInviteRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamInviteService {

    private final TeamInviteRepository teamInviteRepository;
    private final AccountsCommonService accountsCommonService;
    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;
    private final UserJpaRepository userJpaRepository;

    public TeamInviteResponseDto inviteTeamMembers(TeamInviteRequestDto request, User inviter) {
        User invitedUser = userJpaRepository.findById(request.userId()).orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.USER_NOT_FOUND));
        Team team = teamRepository.findById(request.teamId()).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        // 방장이 자기 자신을 초대 못하게 하는 예외처리
        if (inviter.getId().equals(invitedUser.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.CANNOT_INVITE_YOURSELF);
        }

        if (!team.getLeader().getId().equals(inviter.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.NO_AUTHORIZATION);
        }

        TeamInvite existingInvite = teamInviteRepository.findByTeamAndInvitedUser(team, invitedUser);
        if (existingInvite != null) {
            throw new TeamMemberExceptionHandler(ErrorCode.TEAMMEMBER_EXISTS);
        }


        TeamInvite teamInvite = request.toEntity(invitedUser, team, inviter);

        teamInviteRepository.save(teamInvite);
        return TeamInviteResponseDto.from(teamInvite);
    }

    public void kickOutMembers(Long memberId, Long teamId, User user) {
        TeamMember teamMember = teamMembersRepository.findById(memberId).orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER));
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        // 프로필 존재 확인
        accountsCommonService.checkUserProfile(teamMember.getUser());
        //방장이 자기 자신을 강퇴 못하게 해주는 예외처리
        if (user.getId().equals(memberId)) {
            throw new TeamMemberExceptionHandler(ErrorCode._BAD_REQUEST);
        }
        //이미 강퇴당한 팀원 예외처리
        if (teamMember.getId() == null)
            throw new TeamMemberExceptionHandler(ErrorCode.ALREAY_KICKED_OUT);
        //방장에게만 강퇴 권한
        if (team.getLeader().getId().equals(user.getId()))
            teamMembersRepository.delete(teamMember);
        else
            throw new TeamMemberExceptionHandler(ErrorCode.NO_AUTHORIZATION);
    }

    public TeamMembersResponseDto acceptInvite(Long inviteId, User user) {
        TeamInvite invite = teamInviteRepository.findById(inviteId)
                .orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.INVITE_NOT_FOUND));

        if (!invite.getInvitedUser().getId().equals(user.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.WRONG_INVITATION);
        }

        invite.accept();
        teamInviteRepository.save(invite);
        TeamMember teamMember = TeamMember.createTeamMember(user, Role.MEMBER, invite.getTeam());
        teamMembersRepository.save(teamMember);
        return TeamMembersResponseDto.from(teamMember);
    }


    public void rejectInvite(Long inviteId, User user) {
        TeamInvite invite = teamInviteRepository.findById(inviteId)
                .orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.INVITE_NOT_FOUND));

        if (!invite.getInvitedUser().getId().equals(user.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.WRONG_INVITATION);
        }
        if(!invite.getInvitationStatus().equals(InvitationStatus.ACCEPTED) && !invite.getInvitationStatus().equals(InvitationStatus.PENDING))
            teamInviteRepository.delete(invite);
    }
}
