package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;

public record DetailResponse(
        Long detailId,
        Long tacticId,
        Position position,
        String positionDescription
){
    public static DetailResponse from(TacticPositionDetail tacticPositionDetail){
        return new DetailResponse(
                tacticPositionDetail.getDetailId(),
                tacticPositionDetail.getTactic().getTacticId(),
                tacticPositionDetail.getPosition(),
                tacticPositionDetail.getPositionDescription()
        );
    }
}
