package com.capstone.BnagFer.domain.myteam.dto;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CreateTeamTacticResponseDto (
        Long id,
        Long leaderId, //leader_id
        String leaderName, //leader_name
        String teamName,
        List<TeamMember> teamMembers,
        LocalDateTime createdAt,
        TacticDto tacticDto

) {
    public static CreateTeamTacticResponseDto from(Long teamId, Long leaderId, String leaderName, String teamName, List<TeamMember> teamMembers
    , LocalDateTime createdAt, TacticDto tacticDto) {
        return CreateTeamTacticResponseDto.builder()
                .id(teamId)
                .leaderId(leaderId) //null로 넣어두고 service단에서 id값 추가해주기
                .leaderName(leaderName)
                .teamName(teamName)
//                .tacticDto(TacticResponse.from(tactic))
                .teamMembers(null)
                .createdAt(createdAt)
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
        public static TacticDto from(Long tacticId, String tacticName, Boolean anonymous, String famousCoachName, String
                                  mainFormation, byte[] attackFormation, byte[] defenseFormation, String tacticDetails, String attackDetails
                                  , String defenseDetails, LocalDateTime createdAt, LocalDateTime updatedAt
        ) {
            return TacticDto.builder()
                    .tacticId(tacticId)
                    .tacticName(tacticName)
                    .anonymous(anonymous)
                    .famousCoachName(famousCoachName)
                    .mainFormation(mainFormation)
                    .attackFormation(attackFormation)
                    .defenseFormation(defenseFormation)
                    .tacticDetails(tacticDetails)
                    .attackDetails(attackDetails)
                    .defenseDetails(defenseDetails)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();
        }
    }
}