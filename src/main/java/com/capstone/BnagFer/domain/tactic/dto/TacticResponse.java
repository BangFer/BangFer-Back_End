package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record TacticResponse(Long tacticId,
                             Long userId,
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
                             LocalDateTime updatedAt) {
        public static TacticResponse from(Tactic tactic) {
            return new TacticResponse(
                    tactic.getTacticId(),
                    tactic.getUser().getId(),
                    tactic.getTacticName(),
                    tactic.isAnonymous(),
                    tactic.getFamousCoachName(),
                    tactic.getMainFormation(),
                    tactic.getAttackFormation(),
                    tactic.getDefenseFormation(),
                    tactic.getTacticDetails(),
                    tactic.getAttackDetails(),
                    tactic.getDefenseDetails(),
                    tactic.getCreatedAt(),
                    tactic.getUpdatedAt()
            );
        }
    @Builder
    public record TacticList(Long tacticId,
                             Long userId,
                             String tacticName,
                             Boolean anonymous,
                             String famousCoachName,
                             String mainFormation) {
            public static TacticList from(Tactic tactic){
                return TacticList.builder()
                        .tacticId(tactic.getTacticId())
                        .userId(tactic.getUser().getId())
                        .tacticName(tactic.getTacticName())
                        .anonymous(tactic.isAnonymous())
                        .famousCoachName(tactic.getFamousCoachName())
                        .mainFormation(tactic.getMainFormation())
                        .build();
            }
            public static List<TacticList> from(List<Tactic> tactics) {
                return tactics.stream().map(TacticList::from).collect(Collectors.toList());
        }
    }
}
