package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamRequest;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamResponse;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.exception.TeamExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserJpaRepository userRepository;

    public CUTeamResponse.teamDetail createMyTeam(CUTeamRequest.CreateDTO request) {
        User user = userRepository.findByEmail("geunsikevin@gmail.com").orElseThrow(() -> new TeamExceptionHandler(ErrorCode.USER_NOT_FOUND));
        Team team = request.toEntity();
        team.setLeader(user);
        teamRepository.save(team);
        return CUTeamResponse.teamDetail.from(team);
    }

    public CUTeamResponse.teamDetail updateMyTeam(CUTeamRequest.UpdateDTO request) {
        User user = userRepository.findByEmail("geunsikevin@gmail.com").orElseThrow(() -> new TeamExceptionHandler(ErrorCode.USER_NOT_FOUND));
        Team team = teamRepository.findById(user.getId()).orElseThrow(() -> new TeamExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
        team.updateTeam(request);
        Team updatedTeam = teamRepository.save(team);
        return CUTeamResponse.teamDetail.from(updatedTeam);
    }



}
