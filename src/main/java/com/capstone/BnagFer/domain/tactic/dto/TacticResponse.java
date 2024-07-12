package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.myteam.dto.response.CreateTeamTacticResponseDto;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record TacticResponse(
        Long tacticId,
        Long userId,
        String nickname,
        String tacticName,
        Boolean anonymous,
        String famousCoachName,
        String mainFormation,
        List<DetailResponse> positionDetail,
        String tacticDetails,
        String attackDetails,
        String defenseDetails,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static TacticResponse from(Tactic tactic) {
        List<DetailResponse> positionDetail = tactic.getTacticPositionDetails()
                .stream()
                .map(DetailResponse::from)
                .toList();

        return new TacticResponse(
                tactic.getTacticId(),
                tactic.getUser().getId(),
                tactic.getUser().getProfile().getNickname(),
                tactic.getTacticName(),
                tactic.isAnonymous(),
                tactic.getFamousCoachName(),
                tactic.getMainFormation(),
                positionDetail,
                tactic.getTacticDetails(),
                tactic.getAttackDetails(),
                tactic.getDefenseDetails(),
                tactic.getCreatedAt(),
                tactic.getUpdatedAt()
        );
    }

    @Builder
    public record DetailResponse(
            Long detailId,
            Position position,
            String positionDescription
    ) {
        public static DetailResponse from(TacticPositionDetail tacticPositionDetail) {
            return DetailResponse.builder()
                    .detailId(tacticPositionDetail.getDetailId())
                    .position(tacticPositionDetail.getPosition())
                    .positionDescription(tacticPositionDetail.getPositionDescription())
                    .build();
        }
    }

    @Builder
    public record TacticList(Long tacticId,
                             Long userId,
                             String nickname,
                             String tacticName,
                             Boolean anonymous,
                             String famousCoachName,
                             String mainFormation) {
        public static TacticList from(Tactic tactic) {
            return TacticList.builder()
                    .tacticId(tactic.getTacticId())
                    .userId(tactic.getUser().getId())
                    .nickname(tactic.getUser().getProfile().getNickname())
                    .tacticName(tactic.getTacticName())
                    .anonymous(tactic.isAnonymous())
                    .famousCoachName(tactic.getFamousCoachName())
                    .mainFormation(tactic.getMainFormation())
                    .build();
        }

        public static List<TacticList> from(List<Tactic> tactics) {
            return tactics.stream().map(TacticList::from).collect(Collectors.toList());
        }
    }
}
