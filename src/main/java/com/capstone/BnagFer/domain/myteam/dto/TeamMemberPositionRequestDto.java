package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;

public record TeamMemberPositionRequestDto(
        Long teamId,
        Long memberId,
        Position position
) {
    public TeamMember toEntity(Team team, TeamMember teamMember, Position position) {
        return TeamMember.builder()
                .team(teamMember.getTeam())
                .id(teamMember.getId())
                .position(teamMember.getPosition())
                .build();
    }
}
