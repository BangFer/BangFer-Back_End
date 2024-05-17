package com.capstone.BnagFer.domain.board.dto.request;

import com.capstone.BnagFer.domain.board.entity.Comment;
import jakarta.validation.constraints.NotBlank;

public record CommentRequestDto(
        @NotBlank
        String commentText
) {
    public Comment toEntity() {
        return Comment.builder()
                .commentText(commentText)
                .build();
    }
}
