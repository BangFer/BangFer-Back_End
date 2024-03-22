package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import jakarta.validation.constraints.NotBlank;

public record TacticUpdateRequest(
        @NotBlank(message = "[ERROR] 전술명은 필수입니다.")
        String tacticName,
        @NotBlank(message = "[ERROR] 게시물 공개여부는 필수입니다.")
        Boolean anonymous,
        String famousCoachName,
        @NotBlank(message = "[ERROR] 주 포메이션은 필수입니다.")
        String mainFormation,
        byte[] attackFormation,
        byte[] defenseFormation,
        @NotBlank(message = "[ERROR] 전술 설명은 필수입니다.")
        String tacticDetails,
        String attackDetails,
        String defenseDetails
) {}
