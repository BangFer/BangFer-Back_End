package com.capstone.BnagFer.domain.tactic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @NotBlank(message = "[ERROR] 댓글내용은 필수입니다.")
        @Schema(description = "comment", example = "다시 보니 별론데?")
        String comment
) {}
