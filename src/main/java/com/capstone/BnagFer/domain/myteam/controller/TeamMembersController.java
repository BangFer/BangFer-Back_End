package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.myteam.dto.request.TeamMemberPositionRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMemberPositionResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamMemberRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMembersResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamMembersQueryService;
import com.capstone.BnagFer.domain.myteam.service.TeamMembersService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team/members")
@RequiredArgsConstructor
public class TeamMembersController {
    private final TeamMembersService teamMembersService;
    private final TeamMembersQueryService teamMembersQueryService;

    @GetMapping("{teamId}")
    public ApiResponse<List<TeamMembersResponseDto>> getMembers(@PathVariable Long teamId) {
        List<TeamMembersResponseDto> myTeam = teamMembersQueryService.getMembers(teamId);
        return ApiResponse.onSuccess(myTeam);
    }

    @PostMapping("/invite")
    public ApiResponse<TeamMembersResponseDto> addMembers(@RequestBody TeamMemberRequestDto request) {
        TeamMembersResponseDto myTeam = teamMembersService.addTeamMembers(request);
        return ApiResponse.onSuccess(myTeam);
    }

    @DeleteMapping("/kickout/{memberId}")
    public ApiResponse<Object> kickOutMembers(@PathVariable Long memberId) {
        teamMembersService.kickOutMembers(memberId);
        return ApiResponse.noContent();
    }

    @PostMapping("/position")
    public ApiResponse<TeamMemberPositionResponseDto> allocatePosition(@RequestBody TeamMemberPositionRequestDto request) {
        TeamMemberPositionResponseDto position = teamMembersService.allocatePosition(request);
        return ApiResponse.onSuccess(position);
    }

    @DeleteMapping("/position/{teamId}/{memberId}")

    public ApiResponse<Object> deallocatePosition(@PathVariable Long teamId, Long memberId) {
        teamMembersService.deallocatePosition(teamId, memberId);
        return ApiResponse.noContent();
    }
}