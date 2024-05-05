package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.myteam.dto.GetTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryService {
    private final AccountsServiceUtils accountsServiceUtils;
    private final TeamServiceUtils teamServiceUtils;

    public GetTeamResponseDto getMyTeamById(Long teamId) {
        Team team = teamServiceUtils.checkValidTeam(teamId);
        return GetTeamResponseDto.from(team);
    }
    public List<GetTeamResponseDto.TeamList> getMyTeamList() {
        User user = accountsServiceUtils.getCurrentUser();
        List<Team> teams = user.getTeam();
        return GetTeamResponseDto.TeamList.from(teams);
    }
}
