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
import lombok.extern.slf4j.Slf4j;
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
        User user = accountsServiceUtils.getCurrentUser();
        User invitedUser = userJpaRepository.findById(request.userId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Team team = teamRepository.findById(request.teamId()).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        //방장이 자기 자신을 초대 못하게 해주는 예외처리
        if (user.getId().equals(invitedUser.getId())) {
            throw new TeamMemberExceptionHandler(ErrorCode._BAD_REQUEST);
        }
        //이미 초대된 사용자이므로 예외 처리
        TeamMember existingMember = teamMembersRepository.findByTeamAndUser(team, invitedUser);
        if (existingMember != null) {
            throw new TeamMemberExceptionHandler(ErrorCode.TEAMMEMBER_EXISTS);
        }
        //팀원 생성
        TeamMember teamMember = request.toEntity(invitedUser, team);
        teamMember.setRole(Role.MEMBER);
        //팀 리더 생성
//        TeamMember leader = request.toEntity(user, team);
//        //만약 teamMemberRepository에 leader.getUser().getId(0 값 없으면
//        if(teamMembersRepository.findById(leader.getUser().getId()).isEmpty()) {
//            leader.setRole(Role.LEADER);
//            teamMembersRepository.save(leader);
//        }
        teamMembersRepository.save(teamMember);
        return TeamMembersResponseDto.from(teamMember);
    }


    public void kickOutMembers(Long memberId) {
        User user = accountsServiceUtils.getCurrentUser();
        TeamMember teamMember = teamMembersRepository.findById(memberId).orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER));
        Team team = teamRepository.findById(teamMember.getTeam().getId()).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        //방장이 자기 자신을 강퇴 못하게 해주는 예외처리
        if (user.getId().equals(memberId)) {
            throw new TeamMemberExceptionHandler(ErrorCode._BAD_REQUEST);
        }
        //이미 강퇴당한 팀원 예외처리
        if(teamMember.getId()==null)
            throw new TeamMemberExceptionHandler(ErrorCode.ALREAY_KICKED_OUT);
        //방장에게만 강퇴 권한
        if(team.getLeader().getId() == user.getId())
            teamMembersRepository.deleteById(memberId);
        else
            throw new TeamMemberExceptionHandler(ErrorCode.NO_AUTHORIZATION);
    }
}
