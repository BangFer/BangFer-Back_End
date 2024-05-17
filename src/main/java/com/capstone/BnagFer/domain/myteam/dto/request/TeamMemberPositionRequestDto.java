package com.capstone.BnagFer.domain.myteam.dto.request;

import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import jakarta.validation.constraints.NotBlank;

public record TeamMemberPositionRequestDto(
        @NotBlank
        Long teamId,
        @NotBlank
        Long memberId,
        @NotBlank(message = "포지션 할당은 필수입니다.")
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
