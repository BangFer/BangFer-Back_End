package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
public record CommentResponseDto(
        Long id,
        Long boardId,
        Long userId,
        String nickName,
        String commentText,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getCommentId(),
                comment.getBoard().getId(),
                comment.getUser().getId(),
                comment.getUser().getProfile().getNickname(),
                comment.getCommentText(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
