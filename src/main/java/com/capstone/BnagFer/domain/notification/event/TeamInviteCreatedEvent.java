package com.capstone.BnagFer.domain.notification.event;

import com.capstone.BnagFer.domain.myteam.entity.TeamInvite;

public record TeamInviteCreatedEvent(
        Long inviteId,
        Long teamId,
        Long inviterId,
        Long invitedUserId,
        String teamName,
        String inviterNickname
) {
    public TeamInviteCreatedEvent(TeamInvite teamInvite) {
        this(
                teamInvite.getId(),
                teamInvite.getTeam().getId(),
                teamInvite.getInviter().getId(),
                teamInvite.getInvitedUser().getId(),
                teamInvite.getTeam().getTeamName(),
                teamInvite.getInviter().getProfile().getNickname()
        );
    }
}