package com.capstone.BnagFer.domain.myteam.dto.request;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import jakarta.validation.constraints.NotNull;

public record TeamMemberPositionRequestDto(
        Long teamId,
        Long memberId,
        @NotNull(message = "포지션 할당은 필수입니다.")
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
