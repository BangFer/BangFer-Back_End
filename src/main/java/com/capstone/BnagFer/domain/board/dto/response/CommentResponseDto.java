package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CommentResponseDto(
        Long id,
        Long commentWriterId,
        Long boardWriterId,
        String commentText,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .commentWriterId(comment.getUser().getId())
                .boardWriterId(comment.getBoard().getUser().getId())
                .commentText(comment.getCommentText())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
