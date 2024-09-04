package com.capstone.BnagFer.domain.notification.event;

import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import lombok.Getter;

@Getter
public class PositionAllocatedEvent {
    private final Long teamId;
    private final Long memberId;
    private final String teamName;
    private final Position position;

    public PositionAllocatedEvent(TeamMember teamMember, Position position) {
        this.teamId = teamMember.getTeam().getId();
        this.memberId = teamMember.getId();
        this.teamName = teamMember.getTeam().getTeamName();
        this.position = position;
    }
}