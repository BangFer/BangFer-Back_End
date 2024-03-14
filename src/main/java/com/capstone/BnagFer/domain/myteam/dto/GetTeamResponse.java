package com.capstone.BnagFer.domain.myteam.dto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class GetTeamResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class teamDetail {
        private Long id;
        private Long leaderId; //leader_id
        private String leaderName; //leader_name
        private String teamName;
        private Long tacticId;
        private List <TeamMember> teamMembers;

        private LocalDateTime createdAt;
        //DTO에서 다른 객체가 참조되면 null값으로 넣어두고 Service단에서 처리해주는 식으로~
        public static GetTeamResponse.teamDetail from(Team team) {
            return teamDetail.builder()
                    .id(team.getId())
                    .leaderId(null) //null로 넣어두고 service단에서 id값 추가해주기
                    .leaderName(team.getLeader().getName())
                    .tacticId(null)
                    .teamMembers(null)
                    .createdAt(team.getCreatedAt())
                    .build();
        }
    }
}

