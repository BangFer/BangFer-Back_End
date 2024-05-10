package com.capstone.BnagFer.domain.myteam.controller;
import com.capstone.BnagFer.domain.myteam.dto.request.CUTeamRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.CUTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.response.CreateTeamTacticResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.response.GetTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamService;
import com.capstone.BnagFer.domain.myteam.service.TeamTacticQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamTacticService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final TeamQueryService teamQueryService;
    private final TeamService teamService;
    private final TeamTacticService teamTacticService;
    private final TeamTacticQueryService teamTacticQueryService;

    @GetMapping("/{teamId}")
    public ApiResponse<GetTeamResponseDto> getMyTeam(@PathVariable Long teamId) {
        GetTeamResponseDto myTeam = teamQueryService.getMyTeamById(teamId);
        return ApiResponse.onSuccess(myTeam);
    }

    @GetMapping("/list")
    public ApiResponse<List<GetTeamResponseDto.TeamList>> getMyTeamList() {
        List<GetTeamResponseDto.TeamList> teamList = teamQueryService.getMyTeamList();
        return ApiResponse.onSuccess(teamList);

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

    @GetMapping("/tactic/list")
    public ApiResponse<List<CreateTeamTacticResponseDto.MyTacticList>> getMyTactic() {
        List<CreateTeamTacticResponseDto.MyTacticList> myTacticList = teamTacticQueryService.getMyTacticList();
        return ApiResponse.onSuccess(myTacticList);



    }
}
