package com.capstone.BnagFer.domain.myteam.controller;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.request.CUTeamRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.CUTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.response.CreateTeamTacticResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.response.GetTeamResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamService;
import com.capstone.BnagFer.domain.myteam.service.TeamTacticQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamTacticService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import jakarta.validation.Valid;
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
    public ApiResponse<List<GetTeamResponseDto.TeamList>> getMyTeamList(@LoginUser User user) {
        List<GetTeamResponseDto.TeamList> teamList = teamQueryService.getMyTeamList(user);
        return ApiResponse.onSuccess(teamList);

    }

    @PostMapping
    public ApiResponse<CUTeamResponseDto> createMyTeam(@RequestBody @Valid CUTeamRequestDto request, @LoginUser User user) {
        CUTeamResponseDto myTeam = teamService.createMyTeam(request, user);
        return ApiResponse.onSuccess(myTeam);
    }

    @PutMapping("/{teamId}")
    public ApiResponse<CUTeamResponseDto> updateMyTeam(@PathVariable Long teamId, @RequestBody @Valid CUTeamRequestDto request,
                                                       @LoginUser User user) {
        CUTeamResponseDto updatedTeam = teamService.updateMyTeam(request, teamId, user);
        return ApiResponse.onSuccess(updatedTeam);
    }

    @DeleteMapping("/{teamId}")
    public ApiResponse<Object> deleteMyTeam(@PathVariable Long teamId, @LoginUser User user) {
        teamService.deleteMyTeam(teamId, user);
        return ApiResponse.noContent();
    }

    @PostMapping("/tactic/{teamId}/{tacticId}")
    public ApiResponse<CreateTeamTacticResponseDto> addTacticOnTeam(@PathVariable Long teamId, Long tacticId,
                                                                    @LoginUser User user) {
        CreateTeamTacticResponseDto tacticAddedTeam = teamTacticService.addTactic(teamId, tacticId, user);
        return ApiResponse.onSuccess(tacticAddedTeam);
    }

    @GetMapping("/tactic/list")
    public ApiResponse<List<CreateTeamTacticResponseDto.MyTacticList>> getMyTactic(@LoginUser User user) {
        List<CreateTeamTacticResponseDto.MyTacticList> myTacticList = teamTacticQueryService.getMyTacticList(user);
        return ApiResponse.onSuccess(myTacticList);
    }

    @DeleteMapping("/tactic/{teamId}/{tacticId}")
    public ApiResponse<Object>deallocateMyTactic(@PathVariable Long teamId, Long tacticId, @LoginUser User user) {
        teamTacticService.deallocateMyTactic(teamId, tacticId, user);
        return ApiResponse.noContent();
    }
    @GetMapping("/{teamId}/{memberId}/positionDetail")
    public ApiResponse<List<GetTeamResponseDto.getIndividualDetail>> getIndividualDetail(@PathVariable Long teamId, Long memberId, @LoginUser User user) {
        List<GetTeamResponseDto.getIndividualDetail> positionDetail = teamQueryService .getIndividualDetail(teamId, memberId, user);
        return ApiResponse.onSuccess(positionDetail);
    }

}
