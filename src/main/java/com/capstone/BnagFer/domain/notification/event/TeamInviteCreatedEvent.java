package com.capstone.BnagFer.domain.notification.event;

import com.capstone.BnagFer.domain.myteam.entity.TeamInvite;
import lombok.Getter;

@Getter
public class TeamInviteCreatedEvent {
    private final Long inviteId;
    private final Long teamId;
    private final Long inviterId;
    private final Long invitedUserId;
    private final String teamName;
    private final String inviterNickname;

    public TeamInviteCreatedEvent(TeamInvite teamInvite) {
        this.inviteId = teamInvite.getId();
        this.teamId = teamInvite.getTeam().getId();
        this.inviterId = teamInvite.getInviter().getId();
        this.invitedUserId = teamInvite.getInvitedUser().getId();
        this.teamName = teamInvite.getTeam().getTeamName();
        this.inviterNickname = teamInvite.getInviter().getProfile().getNickname();
    }
}