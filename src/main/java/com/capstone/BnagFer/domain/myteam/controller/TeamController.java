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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "팀 API")
@RequestMapping("/team")
public class TeamController {

    private final TeamQueryService teamQueryService;
    private final TeamService teamService;
    private final TeamTacticService teamTacticService;
    private final TeamTacticQueryService teamTacticQueryService;

    @Operation(summary = "내 팀정보 조회")
    @GetMapping("/{teamId}")
    public ApiResponse<GetTeamResponseDto> getMyTeam(@PathVariable Long teamId, @LoginUser User user) {
        GetTeamResponseDto myTeam = teamQueryService.getMyTeamById(teamId, user);
        return ApiResponse.onSuccess(myTeam);
    }

    @Operation(summary = "내 팀 목록 조회")
    @GetMapping("/list")
    public ApiResponse<List<GetTeamResponseDto.TeamList>> getMyTeamList(@LoginUser User user) {
        List<GetTeamResponseDto.TeamList> teamList = teamQueryService.getMyTeamList(user);
        return ApiResponse.onSuccess(teamList);
    }

    @Operation(summary = "팀 생성", description = "팀을 생성하는 기능. 팀을 생성한 유저가 방장이 됩니다. 프로필 생성 후에 팀 생성 가능.")
    @PostMapping
    public ApiResponse<CUTeamResponseDto> createMyTeam(@RequestBody @Valid CUTeamRequestDto request, @LoginUser User user) {
        CUTeamResponseDto myTeam = teamService.createMyTeam(request, user);
        return ApiResponse.onSuccess(myTeam);
    }

    @Operation(summary = "팀 이름 수정", description = "팀을 이름을 수정하는 기능. 방장만이 수정할 수 있음.")
    @PutMapping("/{teamId}")
    public ApiResponse<CUTeamResponseDto> updateMyTeam(@PathVariable Long teamId, @RequestBody @Valid CUTeamRequestDto request,
                                                       @LoginUser User user) {
        CUTeamResponseDto updatedTeam = teamService.updateMyTeam(request, teamId, user);
        return ApiResponse.onSuccess(updatedTeam);
    }

    @Operation(summary = "팀 삭제", description = "팀을 삭제하는 기능. 방장만이 삭제할 수 있음.")
    @DeleteMapping("/{teamId}")
    public ApiResponse<Object> deleteMyTeam(@PathVariable Long teamId, @LoginUser User user) {
        teamService.deleteMyTeam(teamId, user);
        return ApiResponse.noContent();
    }

    @Operation(summary = "팀에 전술 적용", description = "팀에 전술을 적용하는 기능. 방장만이 적용할 수 있음. 내 전술 목록에 등록되어있는 전술만 가져올 수 있음.")
    @PostMapping("/tactic/{teamId}/{tacticId}")
    public ApiResponse<CreateTeamTacticResponseDto> addTacticOnTeam(@PathVariable Long teamId, @PathVariable Long tacticId,
                                                                    @LoginUser User user) {
        CreateTeamTacticResponseDto tacticAddedTeam = teamTacticService.addTactic(teamId, tacticId, user);
        return ApiResponse.onSuccess(tacticAddedTeam);
    }

    @Operation(summary = "팀 전술 목록", description = "팀에 적용할 수 있는 전술 목록. 내 전술 목록에서 가져옴")
    @GetMapping("/tactic/list")
    public ApiResponse<List<CreateTeamTacticResponseDto.MyTacticList>> getMyTactic(@LoginUser User user) {
        List<CreateTeamTacticResponseDto.MyTacticList> myTacticList = teamTacticQueryService.getMyTacticList(user);
        return ApiResponse.onSuccess(myTacticList);
    }

    @Operation(summary = "팀 전술 해제", description = "팀에 적용된 전술을 해제함. 방장만이 가능.")
    @DeleteMapping("/tactic/{teamId}/{tacticId}")
    public ApiResponse<Object>deallocateMyTactic(@PathVariable Long teamId, @PathVariable Long tacticId, @LoginUser User user) {
        teamTacticService.deallocateMyTactic(teamId, tacticId, user);
        return ApiResponse.noContent();
    }
    @GetMapping("/{teamId}/{positionDetailId}")
    public ApiResponse<GetTeamResponseDto.getIndividualDetail> getIndividualDetail(@PathVariable Long teamId, Long positionDetailId) {
        GetTeamResponseDto.getIndividualDetail positionDetail = teamQueryService.getIndividualDetail(teamId, positionDetailId);
        return ApiResponse.onSuccess(positionDetail);
    }

}
