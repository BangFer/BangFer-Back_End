package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTeamRequest {
    private String teamName;

    @Getter
    public static class CreateDTO {
        private String teamName;
        public Team toEntity() {
            return Team.builder()
                    .teamName(teamName)
                    .build();
        }
    }

    @Getter
    public static class UpdateDTO {
        private String teamName;
    }
}