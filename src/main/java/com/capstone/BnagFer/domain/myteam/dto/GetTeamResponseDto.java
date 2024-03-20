package com.capstone.BnagFer.domain.myteam.dto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Builder
public record GetTeamResponseDto (
        Long id,
        Long leaderId, //leader_id
        String leaderName, //leader_name
        String teamName,
        Long tacticId,
        List <TeamMember> teamMembers,
        LocalDateTime createdAt
) {
    //DTO에서 다른 객체가 참조되면 null값으로 넣어두고 Service단에서 처리해주는 식으로~
    public static GetTeamResponseDto from(Team team) {
        return GetTeamResponseDto.builder()
                .id(team.getId())
                .leaderId(null) //null로 넣어두고 service단에서 id값 추가해주기
                .leaderName(team.getLeader().getName())
                .tacticId(null)
                .teamName(team.getTeamName())
                .teamMembers(null)
                .createdAt(team.getCreatedAt())
                .build();
    }
}

