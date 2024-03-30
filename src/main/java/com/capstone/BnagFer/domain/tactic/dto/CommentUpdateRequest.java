package com.capstone.BnagFer.domain.tactic.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @NotBlank(message = "[ERROR] 댓글내용은 필수입니다.")
        String comment
) {}
