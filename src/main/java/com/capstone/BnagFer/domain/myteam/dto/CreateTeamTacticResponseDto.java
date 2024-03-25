package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import lombok.Builder;

@Builder
public record CreateTeamTacticResponseDto (CUTeamResponseDto teamDto, TacticResponse tacticDto) {
    public static CreateTeamTacticResponseDto from(Team team, Tactic tactic) {
        return CreateTeamTacticResponseDto.builder()
                .teamDto(CUTeamResponseDto.from(team))
                .tacticDto(TacticResponse.from(tactic))
                .build();
    }
}