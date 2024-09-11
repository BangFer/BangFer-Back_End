package com.capstone.BnagFer.domain.notification.event;

import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;

public record PositionAllocatedEvent(
        Long teamId,
        Long memberId,
        String teamName,
        Position position
) {
    public PositionAllocatedEvent(TeamMember teamMember, Position position) {
        this(
                teamMember.getTeam().getId(),
                teamMember.getId(),
                teamMember.getTeam().getTeamName(),
                position
        );
    }
}