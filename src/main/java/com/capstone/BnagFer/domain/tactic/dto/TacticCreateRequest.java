package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TacticCreateRequest(
        @NotBlank(message = "[ERROR] 전술명은 필수입니다.")
        @Schema(description = "tacticName", example = "퍼거슨의 전술")
        String tacticName,
        @NotNull(message = "[ERROR] 익명성 입력은 필수입니다.")
        Boolean anonymous,
        @Schema(description = "famousCoachName", example = "퍼거슨")
        String famousCoachName,
        @NotBlank(message = "[ERROR] 주 포메이션은 필수입니다.")
        @Schema(description = "famousCoachName", example = "4-4-2")
        String mainFormation,
        @NotBlank(message = "[ERROR] 전술 설명은 필수입니다.")
        @Schema(description = "tacticDetails", example = "퍼거슨의 레존두 전술로 전방 압박과...")
        String tacticDetails,
        @Schema(description = "attackDetails", example = "공격시 라인을 끌어올리고...")
        String attackDetails,
        @Schema(description = "defenseDetails", example = "수비시 두줄 수비...")
        String defenseDetails,
        @Schema(description = "positionDetails", example = """
            [
                {"position": "Position1", "positionDescription": "Description1"},
                {"position": "Position2", "positionDescription": "Description2"},
                {"position": "Position3", "positionDescription": "Description3"},
                {"position": "Position4", "positionDescription": "Description4"},
                {"position": "Position5", "positionDescription": "Description5"},
                {"position": "Position6", "positionDescription": "Description6"},
                {"position": "Position7", "positionDescription": "Description7"},
                {"position": "Position8", "positionDescription": "Description8"},
                {"position": "Position9", "positionDescription": "Description9"},
                {"position": "Position10", "positionDescription": "Description10"},
                {"position": "Position11", "positionDescription": "Description11"}
            ]
        """)
        List<DetailCreateRequest> positionDetails
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
