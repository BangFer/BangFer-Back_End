package com.capstone.BnagFer.domain.board.dto.request;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.entity.Board;
import jakarta.validation.constraints.NotBlank;

public record BoardRequestDto(
        @NotBlank(message = "게시글 제목은 필수입니다.")
        String boardTitle,
        @NotBlank(message = "게시글 내용은 필수입니다.")
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
