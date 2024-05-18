package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DetailCreateRequest(
        @NotNull(message = "[ERROR] 포지션 할당은 필수입니다.")
        Position position,
        String positionDescription
){
    public TacticPositionDetail toEntity(Tactic tactic){
        return TacticPositionDetail.builder()
                .position(position)
                .positionDescription(positionDescription)
                .tactic(tactic)
                .build();
    }
}
