package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import lombok.*;

import java.time.LocalDateTime;

public class TacticResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class tacticDetail{
        private Long tacticId;
        private Long userId;
        private String tacticName;
        private Boolean anonymous;
        private String famousCoachName;
        private String mainFormation;
        private String tacticDetails;
        private String attackDetails;
        private String defenseDetails;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        public static tacticDetail from(Tactic tactic){
            return tacticDetail.builder()
                    .tacticId(tactic.getTacticId())
                    .userId(tactic.getUser().getId())
                    .tacticName(tactic.getTacticName())
                    .anonymous(tactic.isAnonymous())
                    .famousCoachName(tactic.getFamousCoachName())
                    .mainFormation(tactic.getMainFormation())
                    .tacticDetails(tactic.getTacticDetails())
                    .attackDetails(tactic.getAttackDetails())
                    .defenseDetails(tactic.getDefenseDetails())
                    .createdAt(tactic.getCreatedAt())
                    .updatedAt(tactic.getUpdatedAt())
                    .build();
        }
    }
}
