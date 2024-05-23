package com.capstone.BnagFer.domain.board.dto.request;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Comment;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequestDto(
        @NotBlank(message = "[ERROR] 댓글 내용은 필수입니다.")
        String commentText
) {
    public Comment toEntity(User user, Board board, Comment parent) {
        Comment boardComment = Comment.builder()
                .commentText(commentText)
                .user(user)
                .board(board)
                .build();
        if (parent != null) {
            boardComment.setParent(parent);
        }
        return boardComment;
    }
}
