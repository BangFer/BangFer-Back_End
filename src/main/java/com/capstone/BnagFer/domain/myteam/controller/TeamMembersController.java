package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.myteam.dto.TeamMemberRequestDto;
import com.capstone.BnagFer.domain.myteam.dto.TeamMembersResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamMembersService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team/members")
@RequiredArgsConstructor
public class TeamMembersController {
    private final TeamMembersService teamMembersService;

    @PostMapping("/invite")
    public ApiResponse<TeamMembersResponseDto> addMembers(@RequestBody TeamMemberRequestDto request)
     {
        TeamMembersResponseDto myTeam = teamMembersService.addTeamMembers(request);
        return ApiResponse.onSuccess(myTeam);

    }
}
