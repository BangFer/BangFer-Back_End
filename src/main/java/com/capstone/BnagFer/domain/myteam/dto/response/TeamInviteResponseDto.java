package com.capstone.BnagFer.domain.myteam.dto.response;

import com.capstone.BnagFer.domain.myteam.entity.InvitationStatus;
import com.capstone.BnagFer.domain.myteam.entity.TeamInvite;
import lombok.Builder;

@Builder
public record TeamInviteResponseDto(
        Long inviteId,
        Long teamId,
        Long invitedUserId,
        Long inviterId,
        InvitationStatus invitationStatus) {

    public static TeamInviteResponseDto from(TeamInvite teamInvite) {
        return TeamInviteResponseDto.builder()
                .inviteId(teamInvite.getId())
                .teamId(teamInvite.getTeam().getId())
                .invitedUserId(teamInvite.getInvitedUser().getId())
                .inviterId(teamInvite.getInviter().getId())
                .invitationStatus(teamInvite.getInvitationStatus())
                .build();
    }
}
