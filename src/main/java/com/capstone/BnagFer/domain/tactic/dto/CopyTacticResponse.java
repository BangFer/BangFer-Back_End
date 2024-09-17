package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record CopyTacticResponse(
        Long tacticId,
        Long userId,
        String nickname,
        String tacticName,
        Boolean anonymous,
        String mainFormation,
        List<TacticDetailResponse.DetailList> positionDetail,
        String tacticDetails,
        String subTactic,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static CopyTacticResponse from(Tactic tactic) {

        return CopyTacticResponse.builder()
                .tacticId(tactic.getTacticId())
                .userId(tactic.getUser().getId())
                .nickname(tactic.getUser().getProfile().getNickname())
                .tacticName(tactic.getTacticName())
                .anonymous(tactic.isAnonymous())
                .mainFormation(tactic.getMainFormation())
                .positionDetail(CopyTacticResponse.DetailList.from(tactic.getTacticPositionDetails()))
                .tacticDetails(tactic.getTacticDetails())
                .subTactic(tactic.getSubTactic())
                .createdAt(tactic.getCreatedAt())
                .updatedAt(tactic.getUpdatedAt())
                .build();
    }

    @Builder
    public record DetailList(
            Long detailId,
            Position position,
            String positionDescription
    ){
        public static TacticDetailResponse.DetailList from(TacticPositionDetail tacticPositionDetail){
            return TacticDetailResponse.DetailList.builder()
                    .detailId(tacticPositionDetail.getDetailId())
                    .position(tacticPositionDetail.getPosition())
                    .positionDescription(tacticPositionDetail.getPositionDescription())
                    .build();
        }
        public static List<TacticDetailResponse.DetailList> from(List<TacticPositionDetail> positions){
            return positions.stream().map(TacticDetailResponse.DetailList::from).toList();
        }
    }
}