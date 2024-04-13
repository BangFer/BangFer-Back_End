package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record TacticDetailResponse(
        Long tacticId,
        Long userId,
        String nickname,
        String tacticName,
        Boolean anonymous,
        String famousCoachName,
        String mainFormation,
        byte[] attackFormation,
        byte[] defenseFormation,
        String tacticDetails,
        String attackDetails,
        String defenseDetails,
        List<CommentList> comments,
        int likeCnt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TacticDetailResponse from(Tactic tactic) {
        return TacticDetailResponse.builder()
                .tacticId(tactic.getTacticId())
                .userId(tactic.getUser().getId())
                .nickname(tactic.getUser().getProfile().getNickname())
                .tacticName(tactic.getTacticName())
                .anonymous(tactic.isAnonymous())
                .famousCoachName(tactic.getFamousCoachName())
                .mainFormation(tactic.getMainFormation())
                .attackFormation(tactic.getAttackFormation())
                .defenseFormation(tactic.getDefenseFormation())
                .tacticDetails(tactic.getTacticDetails())
                .attackDetails(tactic.getAttackDetails())
                .defenseDetails(tactic.getDefenseDetails())
                .comments(CommentList.from(tactic.getComments()))
                .likeCnt(tactic.getLikes().size())
                .createdAt(tactic.getCreatedAt())
                .updatedAt(tactic.getUpdatedAt())
                .build();
    }
    @Builder
    public record CommentList(
            Long tacticCommentId,
            Long tacticId,
            Long userId,
            String nickname,
            String comment,
            LocalDateTime createdAt,
            LocalDateTime updateAt
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
                    .build();
        }
        public static List<CommentList> from(List<TacticComment> comments){
            return comments.stream().map(CommentList::from).collect(Collectors.toList());
        }
    }
}
