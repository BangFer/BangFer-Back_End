package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.Role;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import lombok.Builder;

@Builder
public record TeamMembersResponseDto(
        Long memberId,
        Long userId,
        Role role,
        Position position
) {
    public static TeamMembersResponseDto from(TeamMember teamMember) {
        return TeamMembersResponseDto.builder()
                .memberId(teamMember.getId())
                .userId(teamMember.getUser().getId())
                .role(teamMember.getRole())
                .position(teamMember.getPosition())
                .build();
    }
}
