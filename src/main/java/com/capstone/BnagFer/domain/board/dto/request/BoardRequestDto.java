package com.capstone.BnagFer.domain.board.dto.request;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record BoardRequestDto(
        @NotBlank(message = "게시글 제목은 필수입니다.")
        @Schema(description = "boardTitle", example = "6/2일 상명대학교 축구 용병 구합니다.")
        String boardTitle,
        @NotBlank(message = "게시글 내용은 필수입니다.")
        @Schema(description = "boardContent", example = "스트라이커, 수비수 용병 희망자 댓글 주세요.")
        String boardContent
) {
    public Board toEntity(User user) {
        return Board.builder()
                .user(user)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .build();
    }
}
