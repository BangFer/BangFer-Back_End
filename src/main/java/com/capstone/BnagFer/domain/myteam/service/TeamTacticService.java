package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.response.CreateTeamTacticResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
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
public class TeamTacticService {
    private final TeamRepository teamRepository;
    private final TacticRepository tacticRepository;

    public CreateTeamTacticResponseDto addTactic(Long teamId, Long tacticId, User user) {
        if (user.getId() == null) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_FOUND);
        }
        // 전술 찾기
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));
        // 팀 찾기
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        // 사용자 검증 및 전술 업데이트
        validateAndUpdateTeam(team, user, tactic);
        // 팀 저장
        teamRepository.save(team);
        return CreateTeamTacticResponseDto.from(team, CreateTeamTacticResponseDto.TacticDto.from(tactic));
    }

    public void deallocateMyTactic(Long teamId, Long tacticId, User user) {
        tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        if (!team.getLeader().getId().equals(user.getId())) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        if (team.getTactic() != null && team.getTactic().getTacticId().equals(tacticId)) {
            team.deleteMyTactic();
            teamRepository.save(team);
        } else {
            throw new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND);
        }
    }

    private void validateAndUpdateTeam(Team team, User user, Tactic tactic) {
        // 팀 리더 검증
        if (!team.getLeader().getId().equals(user.getId())) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        // 사용자 전술 권한 검증
        if (!user.getTactics().contains(tactic)) {
            throw new TacticExceptionHandler(ErrorCode.TACTIC_NOT_ALLOWED);
        }
        // 팀 정보 업데이트
        team.updateLeaderAndTactic(user, tactic);
    }
}
