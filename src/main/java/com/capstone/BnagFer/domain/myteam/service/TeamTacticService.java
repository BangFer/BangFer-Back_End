package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
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
    private final AccountsServiceUtils accountsServiceUtils;

    public CreateTeamTacticResponseDto addTactic(Long teamId, Long tacticId, User user) {
        if(user.getId()==null) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_FOUND);
        }
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));
        CreateTeamTacticResponseDto.TacticDto tacticDto = CreateTeamTacticResponseDto.TacticDto.from(tactic);
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        team.updateLeaderAndTactic(user, tactic);
        if(!team.getLeader().getId().equals(user.getId())) {
            throw new TeamExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        if(!user.getTactics().contains(tactic)) {
            throw new TacticExceptionHandler(ErrorCode.TACTIC_NOT_ALLOWED);
        }

        else
            teamRepository.save(team);
        return CreateTeamTacticResponseDto.from(team, tacticDto);
    }
}
