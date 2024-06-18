package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamMemberPositionRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMemberPositionResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamMemberRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMembersResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Role;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.exception.TeamMemberExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.repository.TacticPositionDetailRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamMembersService {

    private final AccountsCommonService accountsCommonService;
    private final TeamMembersRepository teamMembersRepository;
    private final UserJpaRepository userJpaRepository;
    private final TeamRepository teamRepository;
    private final TacticPositionDetailRepository tacticPositionDetailRepository;

    public TeamMembersResponseDto inviteTeamMembers(TeamMemberRequestDto request, User user) {

        User invitedUser = userJpaRepository.findById(request.userId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Team team = teamRepository.findById(request.teamId()).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        //방장이 자기 자신을 초대 못하게 해주는 예외처리
        if (user.getId().equals(invitedUser.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode._BAD_REQUEST);
        }

        if (!team.getLeader().getId().equals(user.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.NO_AUTHORIZATION);
        }

        TeamMember existingMember = teamMembersRepository.findByTeamAndUser(team, invitedUser);
        if (existingMember != null) {
            throw new TeamMemberExceptionHandler(ErrorCode.TEAMMEMBER_EXISTS);
        }

        //팀원 생성
        TeamMember teamMember = request.toEntity(invitedUser, team);
        // 프로필 존재 확인
        accountsCommonService.checkUserProfile(teamMember.getUser());
        teamMember.updateRole(Role.MEMBER);
        teamMembersRepository.save(teamMember);

        return TeamMembersResponseDto.from(teamMember);
    }

    public void kickOutMembers(Long memberId, User user) {
        TeamMember teamMember = teamMembersRepository.findById(memberId).orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER));
        Team team = teamRepository.findById(teamMember.getTeam().getId()).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
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
            teamMembersRepository.deleteById(memberId);
        else
            throw new TeamMemberExceptionHandler(ErrorCode.NO_AUTHORIZATION);
    }

    public TeamMemberPositionResponseDto allocatePosition(TeamMemberPositionRequestDto request, Long teamId, Long memberId, User user) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        TeamMember teamMember = teamMembersRepository.findById(memberId).orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER));

        if(!team.getId().equals(teamMember.getTeam().getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER);
        }

        Position requestedPosition = request.position();
        // 요청한 포지션(requestedPosition)이 이미 다른 멤버에게 할당되어 있는지 확인
        TeamMember existingMemberWithPosition = teamMembersRepository.findByTeamAndPosition(team, requestedPosition);
        boolean teamMemberInTeam = teamMembersRepository.existsByTeamAndId(team, memberId);
        if(team.getLeader().getId().equals(user.getId())) {
            if (teamMemberInTeam) {
                if (existingMemberWithPosition == null || !existingMemberWithPosition.equals(teamMember)) {
                    // 이미 다른 멤버가 요청한 포지션을 가지고 있으면 그 멤버의 포지션을 null로 설정
                    if (existingMemberWithPosition != null) {
                        existingMemberWithPosition.updatePosition(null);
                        teamMembersRepository.save(existingMemberWithPosition);
                    }
                    // 요청한 멤버에게 포지션 할당
                    teamMember.updatePosition(requestedPosition);
                    teamMembersRepository.save(teamMember);
                }
            } else
                throw new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER);
        } else
            throw new TeamMemberExceptionHandler(ErrorCode.CANNOT_ALLOCATE);
        return TeamMemberPositionResponseDto.from(request.toEntity(teamMember));
    }

    public void deallocatePosition(Long teamId, Long memberId, User user) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        TeamMember teamMember = teamMembersRepository.findById(memberId).orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER));
        boolean teamMemberInTeam = teamMembersRepository.existsByTeamAndId(team, memberId);
        if(team.getLeader().getId().equals(user.getId())) {
            if (teamMemberInTeam) {
                if (teamMember.getPosition() != null)
                    teamMember.updatePosition(null);
                else
                    throw new TeamMemberExceptionHandler(ErrorCode.POSITION_ALREADY_DEALLOCATED);
            } else
                throw new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER);
        } else
            throw new TeamMemberExceptionHandler(ErrorCode.CANNOT_ALLOCATE);
    }
}