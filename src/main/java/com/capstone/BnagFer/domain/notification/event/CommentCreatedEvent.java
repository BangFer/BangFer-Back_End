package com.capstone.BnagFer.domain.notification.event;

import com.capstone.BnagFer.domain.board.entity.Comment;
import lombok.Getter;

@Getter
public class CommentCreatedEvent {
    private final Long boardId;
    private final Long commentId;
    private final Long authorId;
    private final Long parentCommentId;

    public CommentCreatedEvent(Comment comment) {
        this.boardId = comment.getBoard().getId();
        this.commentId = comment.getCommentId();
        this.authorId = comment.getUser().getId();
        this.parentCommentId = comment.getParent() != null ? comment.getParent().getCommentId() : null;
    }
}