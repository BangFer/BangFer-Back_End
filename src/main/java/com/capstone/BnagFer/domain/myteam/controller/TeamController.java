package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.CreateTeamTacticResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.GetTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamService;
import com.capstone.BnagFer.domain.myteam.service.TeamTacticService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final UserJpaRepository userJpaRepository;
    private final TeamQueryService teamQueryService;
    private final TeamService teamService;
    private final TeamTacticService teamTacticService;

    @GetMapping("/{teamId}")
    public ApiResponse<GetTeamResponseDto> getMyTeam(@PathVariable Long teamId) {
        GetTeamResponseDto myTeam = teamQueryService.getMyTeamById(teamId);
        return ApiResponse.onSuccess(myTeam);
    }

    @PostMapping
    public ApiResponse<CUTeamResponseDto> createMyTeam(@RequestBody CUTeamRequestDto request) {
        CUTeamResponseDto myTeam = teamService.createMyTeam(request);
        return ApiResponse.onSuccess(myTeam);
    }

    @PutMapping("/{teamId}")
    public ApiResponse<CUTeamResponseDto> updateMyTeam(@PathVariable Long teamId, @RequestBody CUTeamRequestDto request) {
        CUTeamResponseDto updatedTeam = teamService.updateMyTeam(request, teamId);
        return ApiResponse.onSuccess(updatedTeam);
    }

    @DeleteMapping("/{teamId}")
    public ApiResponse<Object> deleteMyTeam(@PathVariable Long teamId) {
        teamService.deleteMyTeam(teamId);
        return ApiResponse.noContent();
    }

    @PostMapping("/tactic/{teamId}/{tacticId}")
    public ApiResponse<CreateTeamTacticResponseDto> addTacticOnTeam(@PathVariable Long teamId, Long tacticId) {
        CreateTeamTacticResponseDto tacticAddedTeam = teamTacticService.addTactic(teamId, tacticId);
        return ApiResponse.onSuccess(tacticAddedTeam);
    }
}
