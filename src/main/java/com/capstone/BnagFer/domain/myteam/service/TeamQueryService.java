package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.response.GetTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import com.capstone.BnagFer.domain.tactic.repository.TacticPositionDetailRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryService {
    private final TeamRepository teamRepository;
    private final TacticPositionDetailRepository tacticPositionDetailRepository;
    private final TeamMembersRepository teamMembersRepository;

    public GetTeamResponseDto getMyTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        return GetTeamResponseDto.from(team);
    }
    public List<GetTeamResponseDto.TeamList> getMyTeamList(User user) {
        List<Team> teams = user.getTeam();
        return GetTeamResponseDto.TeamList.from(teams);
    }

    public GetTeamResponseDto.getIndividualDetail getIndividualDetail(Long teamId, Long tacticPositionDetailId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        TacticPositionDetail tacticPositionDetail = tacticPositionDetailRepository.findById(tacticPositionDetailId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.DETAIL_NOT_FOUND));
        List<TacticPositionDetail> tacticPositionDetails = team.getTactic().getTacticPositionDetails();
        TeamMember byTeamAndPosition = teamMembersRepository.findByTeamAndPosition(team, tacticPositionDetail.getPosition());
        if(tacticPositionDetails.contains(tacticPositionDetail)) {
            return GetTeamResponseDto.getIndividualDetail.from(team, tacticPositionDetail, byTeamAndPosition);
        }
        else
            return GetTeamResponseDto.getIndividualDetail.from(team, tacticPositionDetail, null);
    }
}
