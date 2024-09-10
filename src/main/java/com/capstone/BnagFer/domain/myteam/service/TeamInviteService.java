package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.ProfileJpaRepository;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationService;
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
    private final ProfileJpaRepository profileJpaRepository;
    private final FcmNotificationService fcmNotificationService;

    public TeamInviteResponseDto inviteTeamMembers(TeamInviteRequestDto request, User inviter) {
        Profile profile = profileJpaRepository.findByNickname(request.nickName());
        if (profile == null) {
            throw new TeamMemberExceptionHandler(ErrorCode.USER_NOT_FOUND);
        }
        User invitedUser = profile.getUser();

        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        // 초대받은 사용자가 이미 팀 멤버인지 확인한다
        checkIfAlreadyMember(team, invitedUser);

        // 초대받은 사용자가 이미 초대되었는지 확인한다
        checkIfAlreadyInvited(team, invitedUser);

        // 초대 엔티티를 생성하고 저장한다
        TeamInvite teamInvite = request.toEntity(invitedUser, team, inviter);
        teamInviteRepository.save(teamInvite);

        // FCM 알림 전송
        FcmNotificationRequestDto alarmRequestDto = new FcmNotificationRequestDto(
                "팀 초대",
                inviter.getProfile().getNickname() + "님이 " + team.getTeamName() + " 팀에 초대하였습니다."
        );
        fcmNotificationService.sendAlarm(alarmRequestDto, invitedUser.getId());

        return TeamInviteResponseDto.from(teamInvite);
    }

    
    public void kickOutMembers(Long memberId, Long teamId, User user) {
        TeamMember teamMember = teamMembersRepository.findById(memberId).orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        // 프로필 존재 확인
        accountsCommonService.checkUserProfile(teamMember.getUser());
        //방장에게만 강퇴 권한
        if (team.getLeader().getId().equals(user.getId()))
            throw new TeamMemberExceptionHandler(ErrorCode.NO_AUTHORIZATION);

        //방장이 자기 자신을 강퇴 못하게 해주는 예외처리
        if (user.getId().equals(memberId))
            throw new TeamMemberExceptionHandler(ErrorCode.CANNOT_KICK_OUT_YOURSELF);

        //이미 강퇴당한 팀원 예외처리
        if (teamMember.getId() == null)
            throw new TeamMemberExceptionHandler(ErrorCode.ALREAY_KICKED_OUT);

        teamMembersRepository.delete(teamMember);
    }

    public TeamMembersResponseDto acceptInvite(Long inviteId, User user) {
        TeamInvite invite = teamInviteRepository.findById(inviteId)
                .orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.INVITE_NOT_FOUND));

        if (!invite.getInvitedUser().getId().equals(user.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.WRONG_INVITATION);
        }

        TeamMember teamMember = TeamMember.createTeamMember(user, Role.MEMBER, invite.getTeam());
        teamMembersRepository.save(teamMember);
        teamInviteRepository.delete(invite);
        return TeamMembersResponseDto.from(teamMember);
    }

    public void rejectInvite(Long inviteId, User user) {
        TeamInvite invite = teamInviteRepository.findById(inviteId)
                .orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.INVITE_NOT_FOUND));

        if (!invite.getInvitedUser().getId().equals(user.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.WRONG_INVITATION);
        }
        teamInviteRepository.delete(invite);
    }

    private void checkIfAlreadyMember(Team team, User invitedUser) {
        boolean isAlreadyMember = team.getTeamMembers().stream()
                .anyMatch(teamMember -> teamMember.getUser().equals(invitedUser));
        if (isAlreadyMember) {
            throw new TeamMemberExceptionHandler(ErrorCode.TEAMMEMBER_EXISTS);
        }
    }

    private void checkIfAlreadyInvited(Team team, User invitedUser) {
        boolean isAlreadyInvited = teamInviteRepository.existsByTeamAndInvitedUser(team, invitedUser);
        if (isAlreadyInvited) {
            throw new TeamMemberExceptionHandler(ErrorCode.INVITATION_ALREADY_SENT);
        }
    }
}
