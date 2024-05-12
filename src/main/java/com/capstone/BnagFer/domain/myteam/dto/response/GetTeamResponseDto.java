package com.capstone.BnagFer.domain.myteam.dto.response;
import com.capstone.BnagFer.domain.myteam.entity.Role;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import lombok.*;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetTeamResponseDto (
        Long id,
        Long leaderId, //leader_id
        String leaderNickName, //leader_name
        String teamName,
        TacticResponse tacticDto,
        List<TeamMembersList> teamMembers,
        LocalDateTime createdAt
) {

    public static GetTeamResponseDto from(Team team) {
        return GetTeamResponseDto.builder()
                .id(team.getId())
                .leaderId(team.getLeader().getId())
                .leaderNickName(team.getLeader().getProfile().getNickname())
                .teamName(team.getTeamName())
                .teamMembers(TeamMembersList.from(team.getTeamMembers()))
                .tacticDto(team.getTactic() != null ? TacticResponse.from(team.getTactic()) : null)
                .createdAt(team.getCreatedAt())
                .build();
    }

    @Builder
    public record TeamMembersList(
            Long userId,
            String memberNickName,
            Role role,
            Long memberId,
            Position position

    ) {
        public static TeamMembersList from(TeamMember teamMember) {
            return TeamMembersList.builder()
                    .userId(teamMember.getUser().getId())
                    .memberNickName(teamMember.getUser().getProfile().getNickname())
                    .memberId(teamMember.getId())
                    .role(teamMember.getRole())
                    .position(teamMember.getPosition())
                    .build();
        }
        public static List<TeamMembersList> from(List<TeamMember> teamMembers) {
            return teamMembers.stream().map(TeamMembersList::from).collect(Collectors.toList());
        }
    }

    @Builder
    public record TeamList(
            Long teamId,
            String teamName,
            String leaderNickName
    )
    {
        public static TeamList from(Team team) {
            return TeamList.builder()
                    .teamId(team.getId())
                    .teamName(team.getTeamName())
                    .leaderNickName(team.getLeader().getProfile().getNickname())
                    .build();
        }
        public static List<TeamList> from(List<Team> teams) {
            return teams.stream().map(TeamList::from).collect(Collectors.toList());
        }
    }
}


