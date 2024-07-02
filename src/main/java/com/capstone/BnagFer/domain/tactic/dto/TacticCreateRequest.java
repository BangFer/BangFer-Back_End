package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
        @Size(min = 11, max = 11, message = "[ERROR] 포지션 설명은 정확히 11개여야 합니다.")
        @Schema(description = "positionDetails", example = """
            [
                {"positionDescription": "Description for Position 1"},
                {"positionDescription": "Description for Position 2"},
                {"positionDescription": "Description for Position 3"},
                {"positionDescription": "Description for Position 4"},
                {"positionDescription": "Description for Position 5"},
                {"positionDescription": "Description for Position 6"},
                {"positionDescription": "Description for Position 7"},
                {"positionDescription": "Description for Position 8"},
                {"positionDescription": "Description for Position 9"},
                {"positionDescription": "Description for Position 10"},
                {"positionDescription": "Description for Position 11"}
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
