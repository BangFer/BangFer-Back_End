package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TacticDetailResponse(
        Long tacticId,
        Long userId,
        String nickname,
        String tacticName,
        Boolean anonymous,
        String famousCoachName,
        String mainFormation,
        List<DetailList> positionDetail,
        String tacticDetails,
        String attackDetails,
        String defenseDetails,
        List<CommentList> comments,
        Long likeCnt,
        Long commentCnt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TacticDetailResponse from(Tactic tactic, Long likeCnt, Long commentCnt) {
        return TacticDetailResponse.builder()
                .tacticId(tactic.getTacticId())
                .userId(tactic.getUser().getId())
                .nickname(tactic.getUser().getProfile().getNickname())
                .tacticName(tactic.getTacticName())
                .anonymous(tactic.isAnonymous())
                .famousCoachName(tactic.getFamousCoachName())
                .mainFormation(tactic.getMainFormation())
                .positionDetail(DetailList.from(tactic.getTacticPositionDetails()))
                .tacticDetails(tactic.getTacticDetails())
                .attackDetails(tactic.getAttackDetails())
                .defenseDetails(tactic.getDefenseDetails())
                .comments(CommentList.from(tactic.getComments()))
                .likeCnt(likeCnt)
                .commentCnt(commentCnt)
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
        public static DetailList from(TacticPositionDetail tacticPositionDetail){
            return DetailList.builder()
                    .detailId(tacticPositionDetail.getDetailId())
                    .position(tacticPositionDetail.getPosition())
                    .positionDescription(tacticPositionDetail.getPositionDescription())
                    .build();
        }
        public static List<DetailList> from(List<TacticPositionDetail> positions){
            return positions.stream().map(DetailList::from).toList();
        }
    }

    @Builder
    public record CommentList(
            Long tacticCommentId,
            Long tacticId,
            Long userId,
            String nickname,
            String comment,
            LocalDateTime createdAt,
            LocalDateTime updateAt,
            List<CommentList> children
    ){
        public static CommentList from(TacticComment comment){
            return CommentList.builder()
                    .tacticCommentId(comment.getCommentId())
                    .tacticId(comment.getTactic().getTacticId())
                    .userId(comment.getUser().getId())
                    .nickname(comment.getUser().getProfile().getNickname())
                    .comment(comment.getComment())
                    .createdAt(comment.getCreatedAt())
                    .updateAt(comment.getUpdatedAt())
                    .children(comment.getChildren().stream().map(CommentList::from).toList())
                    .build();
        }
        public static List<CommentList> from(List<TacticComment> comments){
            return comments.stream().filter(c -> c.getParent() == null).map(CommentList::from).toList();
        }
    }
}
