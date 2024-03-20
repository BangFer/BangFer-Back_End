package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import jakarta.validation.constraints.NotBlank;

public record TacticRequest() {

    public record CreateDTO(
            @NotBlank(message = "[ERROR] 전술명은 필수입니다.")
            String tacticName,
            @NotBlank(message = "[ERROR] 게시물 공개여부는 필수입니다.")
            Boolean anonymous,
            String famousCoachName,
            @NotBlank(message = "[ERROR] 주 포메이션은 필수입니다.")
            String mainFormation,
            @NotBlank(message = "[ERROR] 이메일 입력은 필수입니다.")
            String tacticDetails,
            String attackDetails,
            String defenseDetails
    ) {
        public Tactic toEntity(User user) {
            return Tactic.builder()
                    .tacticName(tacticName)
                    .user(user)
                    .anonymous(anonymous)
                    .famousCoachName(famousCoachName)
                    .mainFormation(mainFormation)
                    .tacticDetails(tacticDetails)
                    .attackDetails(attackDetails)
                    .defenseDetails(defenseDetails)
                    .build();
        }
    }

    public record UpdateDTO(
            String tacticName
    ) {}
}




/*
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
*/
