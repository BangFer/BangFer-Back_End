package com.capstone.BnagFer.domain.myteam.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.request.TeamInviteRequestDto;

import com.capstone.BnagFer.domain.myteam.dto.response.TeamInviteResponseDto;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamMembersResponseDto;
import com.capstone.BnagFer.domain.myteam.service.TeamInviteService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "팀 멤버 초대 API")
public class TeamInviteController {
    private final TeamInviteService teamInviteService;

    @Operation(summary = "멤버 초대", description = "팀에 멤버를 초대하는 기능. 방장만이 초대 가능. 팀 멤버는 프로필에 생성된 후에 참여 가능")
    @PostMapping("/invite")
    public ApiResponse<TeamInviteResponseDto> addMembers(@RequestBody @Valid TeamInviteRequestDto request, @LoginUser User user) {
        TeamInviteResponseDto myTeam = teamInviteService.inviteTeamMembers(request, user);
        return ApiResponse.onSuccess(myTeam);
    }

    @Operation(summary = "멤버 강퇴", description = "팀 멤버를 강제로 추방하는 기능. 방장만이 강퇴 가능")
    @DeleteMapping("/kickout/{teamId}/{memberId}")
    public ApiResponse<Object> kickOutMembers(@PathVariable(name = "memberId") Long memberId, @PathVariable (name = "teamId") Long teamId, @LoginUser User user) {
        teamInviteService.kickOutMembers(memberId, teamId,user);
        return ApiResponse.noContent();
    }

    @Operation(summary = "초대 승인", description = "팀 초대를 승인하는 기능")
    @PostMapping("/{inviteId}/accept")
    public ApiResponse<TeamMembersResponseDto> acceptInvite(@PathVariable Long inviteId, @LoginUser User user) {
        TeamMembersResponseDto acceptInvitation = teamInviteService.acceptInvite(inviteId, user);
        return ApiResponse.onSuccess(acceptInvitation);
    }


    @Operation(summary = "초대 거절", description = "팀 초대를 거절하는 기능")
    @PostMapping("/{inviteId}/reject")
    public ApiResponse<Object> rejectInvite(@PathVariable Long inviteId, @LoginUser User user) {
        teamInviteService.rejectInvite(inviteId, user);
        return ApiResponse.noContent();
    }
}

