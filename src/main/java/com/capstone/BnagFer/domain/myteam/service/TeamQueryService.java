package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.response.GetTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryService {

    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;

    public GetTeamResponseDto getMyTeamById(Long teamId, User user) {

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));

        boolean isMember = team.getTeamMembers().stream()
                .anyMatch(teamMember -> teamMember.getUser().equals(user));

        if (!isMember) {
            throw new TeamExceptionHandler(ErrorCode.NO_AUTHORIZATION);
        }

        return GetTeamResponseDto.from(team);
    }

    public List<GetTeamResponseDto.TeamList> getMyTeamList(User user) {

        List<TeamMember> teamMembers = teamMembersRepository.findByUser(user);
        List<GetTeamResponseDto.TeamList> teamLists = new ArrayList<>();

        for (TeamMember teamMember : teamMembers) {
            Team team = teamMember.getTeam();
            teamLists.add(GetTeamResponseDto.TeamList.from(team));
        }

        return teamLists;
    }
}
