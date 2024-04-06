package com.capstone.BnagFer.domain.myteam.dto;
import com.capstone.BnagFer.domain.myteam.entity.Team;

public record CUTeamRequestDto (String teamName) {
    public Team toEntity() {
        return Team.builder()
                .teamName(teamName)
                .build();
    }
}
