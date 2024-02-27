package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import lombok.*;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

import java.time.LocalDateTime;

public class TeamResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class teamDetail {
        private Long id;
        private User leaderId; //leader_id
        private String leaderName; //leader_name
        private String teamName;
        private LocalDateTime createdAt;
        public static teamDetail from(Team team) {
            return teamDetail.builder()
                    .id(team.getId())
                    .leaderId(team.getLeaderId())
                    .leaderName(team.getLeaderId().getName())
                    .createdAt(team.getCreatedAt())
                    .build();
        }
    }
}