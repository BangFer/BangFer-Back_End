package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import lombok.Builder;

@Builder
public record TeamMemberPositionResponseDto (
        Long teamId,
        Long memberId,
        String memberNickName,
        Long leaderId,
        Position position
) {
    public static TeamMemberPositionResponseDto from(TeamMember teamMember) {
        return TeamMemberPositionResponseDto.builder()
                .teamId(teamMember.getTeam().getId())
                .memberId(teamMember.getId())
                .memberNickName(teamMember.getUser().getProfile().getNickname())
                .leaderId(teamMember.getTeam().getLeader().getId())
                .position(teamMember.getPosition())
                .build();
    }
}
