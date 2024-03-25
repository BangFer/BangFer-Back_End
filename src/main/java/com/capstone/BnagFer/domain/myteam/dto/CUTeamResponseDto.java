package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import lombok.*;
import java.time.LocalDateTime;
@Builder
public record CUTeamResponseDto (
        Long id,
        Long leaderId, //leader_id
        String leaderName, //leader_name
        String teamName,
        LocalDateTime createdAt
) {
    public static CUTeamResponseDto from(Team team) {
        return CUTeamResponseDto.builder()
                .id(team.getId())
                .leaderId(team.getLeader().getId())
                .leaderName(team.getLeader().getName())
                .teamName(team.getTeamName())
                .createdAt(team.getCreatedAt())
                .build();
    }
}