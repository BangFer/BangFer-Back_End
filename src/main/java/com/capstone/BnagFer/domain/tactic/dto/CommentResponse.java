package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.TacticComment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long tacticCommentId,
        Long tacticId,
        Long userId,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {
    public static CommentResponse from(TacticComment tacticComment){
        return new CommentResponse(
                tacticComment.getCommentId(),
                tacticComment.getTactic().getTacticId(),
                tacticComment.getUser().getId(),
                tacticComment.getComment(),
                tacticComment.getCreatedAt(),
                tacticComment.getUpdatedAt()
        );
    }
}
