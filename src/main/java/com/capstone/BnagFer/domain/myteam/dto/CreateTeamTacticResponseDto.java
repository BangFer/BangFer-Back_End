package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record CreateTeamTacticResponseDto (
        Long id,
        Long leaderId, //leader_id
        String leaderName, //leader_name
        String teamName,
        LocalDateTime createdAt,
        TacticDto tacticDto
) {
//    public static CreateTeamTacticResponseDto from(Long teamId, Long leaderId, String leaderName, String teamName, List<TeamMember> teamMembers
//    , LocalDateTime createdAt, TacticDto tacticDto) {
//        return CreateTeamTacticResponseDto.builder()
//                .id(teamId)
//                .leaderId(leaderId) //null로 넣어두고 service단에서 id값 추가해주기
//                .leaderName(leaderName)
//                .teamName(teamName)
//                .createdAt(createdAt)
//                .tacticDto(tacticDto)
//                .build();
//    }

    public static CreateTeamTacticResponseDto from(Team team, TacticDto tacticDto){
        return CreateTeamTacticResponseDto.builder()
                .id(team.getId()) // Team의 id 사용
                .leaderId(team.getLeader().getId()) // Team의 leaderId 사용
                .leaderName(team.getLeader().getName()) // Team의 leaderName 사용
                .teamName(team.getTeamName()) // Team의 teamName 사용
                .createdAt(team.getCreatedAt())
                .tacticDto(tacticDto)
                .build();

    }

    @Builder
    public record TacticDto(
            Long tacticId,
            String tacticName,
            Boolean anonymous,
            String famousCoachName,
            String mainFormation,
            byte[] attackFormation,
            byte[] defenseFormation,
            String tacticDetails,
            String attackDetails,
            String defenseDetails,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static TacticDto from(Tactic tactic) {
            return TacticDto.builder()
                    .tacticId(tactic.getTacticId())
                    .tacticName(tactic.getTacticName())
                    .anonymous(tactic.isAnonymous())
                    .famousCoachName(tactic.getFamousCoachName())
                    .mainFormation(tactic.getMainFormation())
                    .attackFormation(tactic.getAttackFormation())
                    .defenseFormation(tactic.getDefenseFormation())
                    .tacticDetails(tactic.getTacticDetails())
                    .attackDetails(tactic.getAttackDetails())
                    .defenseDetails(tactic.getDefenseDetails())
                    .createdAt(tactic.getCreatedAt())
                    .updatedAt(tactic.getUpdatedAt())
                    .build();
        }
    }
    @Builder
    public record MyTacticList(
            Long tacticId,
            String title,
            String formation
    ){
        public static MyTacticList from(Tactic tactic) {
            return MyTacticList.builder()
                    .tacticId(tactic.getTacticId())
                    .title(tactic.getTacticName())
                    .formation(tactic.getMainFormation())
                    .build();
        }
        public static List<MyTacticList> from(List<Tactic> tactics) {
            return tactics.stream().map(MyTacticList::from).collect(Collectors.toList());
        }
    }
}