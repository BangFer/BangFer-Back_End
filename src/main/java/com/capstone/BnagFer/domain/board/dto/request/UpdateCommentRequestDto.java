package com.capstone.BnagFer.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequestDto(
        @NotBlank(message = "[ERROR] 댓글 내용 입력은 필수입니다.")
        String commentText
) {}
