package com.capstone.BnagFer.domain.notification.event;

import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import lombok.Getter;

@Getter
public class TacticCommentCreatedEvent {

    private final Long tacticId;
    private final Long commentId;
    private final Long authorId;
    private final Long parentCommentId;

    public TacticCommentCreatedEvent(TacticComment comment) {
        this.tacticId = comment.getTactic().getTacticId();
        this.commentId = comment.getCommentId();
        this.authorId = comment.getUser().getId();
        this.parentCommentId = comment.getParent() != null ? comment.getParent().getCommentId() : null;
    }
}