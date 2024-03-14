package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TacticRequest {
    @Getter
    public static class CreateDTO{
        private String tacticName;
        private Boolean anonymous;
        private String famousCoachName;
        private String mainFormation;
        private String tacticDetails;
        private String attackDetails;
        private String defenseDetails;

        public Tactic toEntity(){
            return Tactic.builder()
                    .tacticName(tacticName)
                    .anonymous(anonymous)
                    .famousCoachName(famousCoachName)
                    .mainFormation(mainFormation)
                    .tacticDetails(tacticDetails)
                    .attackDetails(attackDetails)
                    .defenseDetails(defenseDetails)
                    .build();
        }
    }
    @Getter
    public static class UpdateDTO {
        private String tacticName;
    }

}
