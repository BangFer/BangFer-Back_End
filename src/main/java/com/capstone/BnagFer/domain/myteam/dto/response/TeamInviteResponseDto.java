package com.capstone.BnagFer.domain.myteam.dto.response;

import com.capstone.BnagFer.domain.myteam.entity.TeamInvite;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record TeamInviteResponseDto(
        Long inviteId,
        Long teamId,
        Long invitedUserId,
        String nickName,
        Long inviterId) {

    public static TeamInviteResponseDto from(TeamInvite teamInvite) {
        return TeamInviteResponseDto.builder()
                .inviteId(teamInvite.getId())
                .teamId(teamInvite.getTeam().getId())
                .invitedUserId(teamInvite.getInvitedUser().getId())
                .nickName(teamInvite.getInvitedUser().getProfile().getNickname())
                .inviterId(teamInvite.getInviter().getId())
                .build();
    }
    public static List<TeamInviteResponseDto> from(List<TeamInvite> teamInvites) {
        return teamInvites.stream().map(TeamInviteResponseDto::from).collect(Collectors.toList());
    }
}
