package com.capstone.BnagFer.domain.myteam.dto;
import com.capstone.BnagFer.domain.myteam.entity.Role;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetTeamResponseDto (
        Long id,
        Long leaderId, //leader_id
        String leaderName, //leader_name
        String teamName,
        TacticResponse tacticDto,
        List<TeamMembersList> teamMembers,
        LocalDateTime createdAt
) {


    //DTO에서 다른 객체가 참조되면 null값으로 넣어두고 Service단에서 처리해주는 식으로~
//    public static GetTeamResponseDto from(Long teamId, Long leaderId, String leaderName, String teamName, List<TeamMembersList> teamMembers, Tactic tacticDto, LocalDateTime createdAt) {
//        return GetTeamResponseDto.builder()
//                .id(teamId)
//                .leaderId(leaderId) //null로 넣어두고 service단에서 id값 추가해주기
//                .leaderName(leaderName)
//                .teamName(teamName)
//                .teamMembers(teamMembers)
//                .tacticDto(TacticResponse.from(tacticDto))
//                .createdAt(createdAt)
//                .build();
//    }

    public static GetTeamResponseDto from(Team team) {
        return GetTeamResponseDto.builder()
                .id(team.getId())
                .leaderId(team.getLeader().getId())
                .leaderName(team.getLeader().getName())
                .teamName(team.getTeamName())
                .teamMembers(TeamMembersList.from(team.getTeamMembers()))
                .tacticDto(TacticResponse.from(team.getTactic()))
                .createdAt(team.getCreatedAt())
                .build();
    }


    @Builder
    public record TeamMembersList(
            Long userId,
            Role role,
            Position position

    ) {
        public static TeamMembersList from(TeamMember teamMember) {
            return TeamMembersList.builder()
                    .userId(teamMember.getUser().getId())
                    .role(teamMember.getRole())
                    .position(teamMember.getPosition())
                    .build();
        }
        public static List<TeamMembersList> from(List<TeamMember> teamMembers) {
            return teamMembers.stream().map(TeamMembersList::from).collect(Collectors.toList());
        }




    }
}


