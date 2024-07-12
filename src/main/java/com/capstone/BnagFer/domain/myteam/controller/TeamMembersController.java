package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamMemberPositionRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMemberPositionResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMembersResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamMembersQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamMembersService;
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
@Tag(name = "팀 멤버 API")
@RequestMapping("/team/members")
public class TeamMembersController {

    private final TeamMembersService teamMembersService;
    private final TeamMembersQueryService teamMembersQueryService;

    @Operation(summary = "팀 멤버 목록 조회")
    @GetMapping("{teamId}")
    public ApiResponse<List<TeamMembersResponseDto>> getMembers(@PathVariable(name = "teamId") Long teamId) {
        List<TeamMembersResponseDto> myTeam = teamMembersQueryService.getMembers(teamId);
        return ApiResponse.onSuccess(myTeam);
    }

    @Operation(summary = "멤버 포지션 할당", description = "포지션에 멤버를 할당하는 기능. 방장만이 할당 가능")
    @PostMapping("/{teamId}/{memberId}/position")
    public ApiResponse<TeamMemberPositionResponseDto> allocatePosition(@RequestBody @Valid TeamMemberPositionRequestDto request,
                                                                       @PathVariable(name = "teamId") Long teamId,
                                                                       @PathVariable(name = "memberId") Long memberId,
                                                                       @LoginUser User user) {
        TeamMemberPositionResponseDto position = teamMembersService.allocatePosition(request, teamId, memberId, user);
        return ApiResponse.onSuccess(position);
    }

    @Operation(summary = "멤버 포지션 할당 해제", description = "포지션에 멤버를 할당 해제 하는 기능. 방장만이 할당 해제 가능")
    @DeleteMapping("/position/{teamId}/{memberId}")
    public ApiResponse<Object> deallocatePosition(@PathVariable(name = "teamId") Long teamId,
                                                  @PathVariable(name = "memberId") Long memberId, @LoginUser User user) {
        teamMembersService.deallocatePosition(teamId, memberId, user);
        return ApiResponse.noContent();
    }
}