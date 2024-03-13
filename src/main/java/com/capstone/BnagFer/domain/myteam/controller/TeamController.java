package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamRequest;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamResponse;
import com.capstone.BnagFer.domain.myteam.dto.GetTeamResponse;
import com.capstone.BnagFer.domain.myteam.service.TeamQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final UserJpaRepository userJpaRepository;
    private final TeamQueryService teamQueryService;
    private final TeamService teamService;

    @GetMapping("/{teamId}")
    public ApiResponse<GetTeamResponse.teamDetail> getMyTeam(@PathVariable Long teamId) {
        GetTeamResponse.teamDetail myTeam = teamQueryService.getMyTeamById(teamId);
        return ApiResponse.onSuccess(myTeam);
    }

    @PostMapping
    public ApiResponse<CUTeamResponse.teamDetail> createMyTeam(@RequestBody CUTeamRequest.CreateDTO request) {
        CUTeamResponse.teamDetail myTeam = teamService.createMyTeam(request);
        return ApiResponse.onSuccess(myTeam);
    }

    @PutMapping("/{teamId}")
    public ApiResponse<CUTeamResponse.teamDetail> updateMyTeam(@PathVariable Long teamId, String userEmail, @RequestBody CUTeamRequest.UpdateDTO request) {
        CUTeamResponse.teamDetail updatedTeam = teamService.updateMyTeam(request);
        return ApiResponse.onSuccess(updatedTeam);
    }

    @DeleteMapping("/{teamId}")
    public ApiResponse<Object> deleteMyTeam(@PathVariable Long teamId) {
        teamService.deleteMyTeam(teamId);
        return ApiResponse.noContent();
    }
}
