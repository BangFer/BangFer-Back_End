package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
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

    public TeamMemberPositionResponseDto allocatePosition(TeamMemberPositionRequestDto request, Long teamId, Long memberId, User user) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        TeamMember teamMember = teamMembersRepository.findById(memberId).orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER));

        if(!team.getId().equals(teamMember.getTeam().getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode.TEAMMEMBER_NOT_IN_TEAM);
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