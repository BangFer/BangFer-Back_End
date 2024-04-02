package com.capstone.BnagFer.domain.myteam.service;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.myteam.dto.GetTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.TeamMembersResponseDto;
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
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryService {
    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;
    private final AccountsServiceUtils accountsServiceUtils;

    public GetTeamResponseDto getMyTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        return GetTeamResponseDto.from(team.getId(), team.getLeader().getId(), team.getLeader().getName(), team.getTeamName(), GetTeamResponseDto.TeamMembersList.from(team.getTeamMembers()), team.getTactic(), team.getCreatedAt());
    }
}
