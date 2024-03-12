package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.myteam.dto.CreateTeamRequest;
import com.capstone.BnagFer.domain.myteam.dto.CreateTeamResponse;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserJpaRepository userRepository;

    public CreateTeamResponse.teamDetail createMyTeam(CreateTeamRequest.CreateDTO request) {
        User user = userRepository.findByEmail("geunsikevin@gmail.com").orElseThrow(() -> new TeamExceptionHandler(ErrorCode.USER_NOT_FOUND));
        Team team = request.toEntity();
        team.setLeader(user);
        teamRepository.save(team);
        return CreateTeamResponse.teamDetail.from(team);
    }



}
