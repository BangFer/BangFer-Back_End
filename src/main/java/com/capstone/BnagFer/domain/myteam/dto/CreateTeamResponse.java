package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import lombok.*;
import java.time.LocalDateTime;

public class CreateTeamResponse {
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
                    .leaderId(null) //null로 넣어두고 service단에서 id값 추가해주기
                    .leaderName(team.getLeaderId().getName())
                    .createdAt(team.getCreatedAt())
                    .build();
        }
    }
}