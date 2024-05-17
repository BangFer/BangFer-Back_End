package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TacticCreateRequest(
        @NotBlank(message = "[ERROR] 전술명은 필수입니다.")
        String tacticName,
        @NotNull(message = "[ERROR] 익명성 입력은 필수입니다.")
        Boolean anonymous,
        String famousCoachName,
        @NotBlank(message = "[ERROR] 주 포메이션은 필수입니다.")
        String mainFormation,
        byte[] attackFormation,
        byte[] defenseFormation,
        @NotBlank(message = "[ERROR] 전술 설명은 필수입니다.")
        String tacticDetails,
        String attackDetails,
        String defenseDetails,
        List<DetailCreateRequest> positionDetails
) {
    public Tactic toEntity(User user) {
        return Tactic.builder()
                .tacticName(tacticName)
                .user(user)
                .anonymous(anonymous)
                .famousCoachName(famousCoachName)
                .mainFormation(mainFormation)
                .attackFormation(attackFormation)
                .defenseFormation(defenseFormation)
                .tacticDetails(tacticDetails)
                .attackDetails(attackDetails)
                .defenseDetails(defenseDetails)
                .build();
    }
}
