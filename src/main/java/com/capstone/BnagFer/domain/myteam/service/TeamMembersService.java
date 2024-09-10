package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationService;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamMemberPositionRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMemberPositionResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.exception.TeamMemberExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamMembersService {
    private final TeamMembersRepository teamMembersRepository;
    private final TeamRepository teamRepository;
    private final FcmNotificationService fcmNotificationService;

    public TeamMemberPositionResponseDto allocatePosition(TeamMemberPositionRequestDto request, Long teamId, Long memberId, User user) {
        Team team = findTeamById(teamId);
        TeamMember teamMember = findTeamMemberById(memberId);

        validateTeamMemberIsInTeam(team, teamMember);
        validateLeaderAuthorization(team, user);

        Position requestedPosition = request.position();
        TeamMember existingMemberWithPosition = findExistingMemberWithPosition(team, requestedPosition);

        allocateTeamMemberPosition(team, teamMember, existingMemberWithPosition, requestedPosition);

        return TeamMemberPositionResponseDto.from(request.toEntity(teamMember));
    }

    public void deallocatePosition(Long teamId, Long memberId, User user) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        TeamMember teamMember = teamMembersRepository.findById(memberId).orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER));
        boolean teamMemberInTeam = teamMembersRepository.existsByTeamAndId(team, memberId);
        if (team.getLeader().getId().equals(user.getId())) {
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

    private Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
    }

    private TeamMember findTeamMemberById(Long memberId) {
        return teamMembersRepository.findById(memberId)
                .orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER));
    }

    private void validateTeamMemberIsInTeam(Team team, TeamMember teamMember) {
        if (!team.getId().equals(teamMember.getTeam().getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.TEAMMEMBER_NOT_IN_TEAM);
        }
    }

    private void validateLeaderAuthorization(Team team, User user) {
        if (!team.getLeader().getId().equals(user.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.CANNOT_ALLOCATE);
        }
    }

    private TeamMember findExistingMemberWithPosition(Team team, Position requestedPosition) {
        return teamMembersRepository.findByTeamAndPosition(team, requestedPosition);
    }

    private void allocateTeamMemberPosition(Team team, TeamMember teamMember, TeamMember existingMemberWithPosition, Position requestedPosition) {
        boolean teamMemberInTeam = teamMembersRepository.existsByTeamAndId(team, teamMember.getId());

        if (!teamMemberInTeam) {
            throw new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER);
        }

        if (existingMemberWithPosition != null && !existingMemberWithPosition.equals(teamMember)) {
            existingMemberWithPosition.updatePosition(null);
            teamMembersRepository.save(existingMemberWithPosition);
        }

        teamMember.updatePosition(requestedPosition);
        teamMembersRepository.save(teamMember);

        sendPositionAllocationNotification(team, teamMember, requestedPosition);
    }

    private void sendPositionAllocationNotification(Team team, TeamMember teamMember, Position requestedPosition) {
        FcmNotificationRequestDto alarmRequestDto = new FcmNotificationRequestDto(
                "포지션 할당",
                team.getTeamName() + " 팀에서 " + requestedPosition.name() + " 포지션이 할당되었습니다."
        );
        fcmNotificationService.sendAlarm(alarmRequestDto, teamMember.getUser().getId());
    }
}