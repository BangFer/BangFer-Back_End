package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import lombok.*;
import java.time.LocalDateTime;

public class CUTeamResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class teamDetail {
        private Long id;
        private Long leaderId; //leader_id
        private String leaderName; //leader_name
        private String teamName;
        private LocalDateTime createdAt;
        public static teamDetail from(Team team) {
            return teamDetail.builder()
                    .id(team.getId())
                    .leaderId(team.getLeader().getId())
                    .leaderName(team.getLeader().getName())
                    .createdAt(team.getCreatedAt())
                    .build();
        }
    }
}