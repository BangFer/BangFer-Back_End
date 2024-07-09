package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;

public record DetailCreateRequest(
        String positionDescription
){
    public TacticPositionDetail toEntity(Tactic tactic, int positionIndex){
        Position position = Position.values()[positionIndex];
        return TacticPositionDetail.builder()
                .position(position)
                .positionDescription(positionDescription)
                .tactic(tactic)
                .build();
    }
}
