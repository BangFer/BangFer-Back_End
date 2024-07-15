package com.capstone.BnagFer.domain.myteam.dto.response;
import com.capstone.BnagFer.domain.myteam.entity.Role;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import com.google.firebase.database.annotations.Nullable;
import lombok.*;

import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetTeamResponseDto (
        Long id,
        Long leaderId,
        String leaderNickName,
        String teamName,
        List<TeamTacticDetailResponse> tacticDto,
        List<TeamMembersList> teamMembers,
        LocalDateTime createdAt
) {
    @Builder
    public record TeamTacticDetailResponse(
            Long detailId,
            Position position,
            String positionDescription
    ) {
        public static TeamTacticDetailResponse from(TacticPositionDetail tacticPositionDetail){
            return TeamTacticDetailResponse.builder()
                    .detailId(tacticPositionDetail.getDetailId())
                    .position(tacticPositionDetail.getPosition())
                    .positionDescription(tacticPositionDetail.getPositionDescription())
                    .build();
        }
        public static List<TeamTacticDetailResponse> from(List<TacticPositionDetail> positions){
            return positions.stream().map(TeamTacticDetailResponse::from).toList();
        }
    }

    public static GetTeamResponseDto from(Team team) {
        return GetTeamResponseDto.builder()
                .id(team.getId())
                .leaderId(team.getLeader().getId())
                .leaderNickName(team.getLeader().getProfile().getNickname())
                .teamName(team.getTeamName())
                .teamMembers(TeamMembersList.from(team.getTeamMembers()))
                .tacticDto(TeamTacticDetailResponse.from(team.getTactic().getTacticPositionDetails()))
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
            Long teamMemberId,
            String leaderNickName
    ) {
        public static TeamList from(Team team, TeamMember teamMember) {
            return TeamList.builder()
                    .teamId(team.getId())
                    .teamName(team.getTeamName())
                    .teamMemberId(teamMember.getId())
                    .leaderNickName(team.getLeader().getProfile().getNickname())
                    .build();
        }
    }
    @Builder
    public record getIndividualDetail(
            Long teamId,
            Long tacticPositionDetailId,
            String memberNickName
    ) {
        public static getIndividualDetail from(Team team, TacticPositionDetail tacticPositionDetail, @Nullable TeamMember teamMember) {
            return getIndividualDetail.builder()
                    .teamId(team.getId())
                    .tacticPositionDetailId(tacticPositionDetail.getDetailId())
                    .memberNickName(teamMember != null && teamMember.getUser() != null && teamMember.getUser().getProfile() != null
                            ? teamMember.getUser().getProfile().getNickname()
                            : null)
                    .build();

        }
    }
}

