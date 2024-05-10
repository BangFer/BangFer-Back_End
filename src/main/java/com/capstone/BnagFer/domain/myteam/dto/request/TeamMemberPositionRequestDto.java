package com.capstone.BnagFer.domain.myteam.dto.request;

import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
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
                .user(teamMember.getUser())
                .id(teamMember.getId())
                .position(teamMember.getPosition())
                .build();
    }
}
