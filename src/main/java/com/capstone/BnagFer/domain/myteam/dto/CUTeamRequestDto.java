package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;


public record CUTeamRequestDto (String teamName) {
    public Team toEntity() {
        return Team.builder()
                .teamName(teamName)
                .build();
    }
}