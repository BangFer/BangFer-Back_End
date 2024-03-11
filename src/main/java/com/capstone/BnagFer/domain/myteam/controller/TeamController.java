package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.myteam.dto.TeamResponse;
import com.capstone.BnagFer.domain.myteam.service.TeamService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

//    @GetMapping("/{teamId}")
//    public ApiResponse<TeamResponse.teamDetail> getMyTeam(@PathVariable Long teamId) {
//        TeamResponse.teamDetail myTeam = teamService.getMyTeamById(teamId);
//        return ApiResponse.onSuccess(myTeam);
//    }
}
