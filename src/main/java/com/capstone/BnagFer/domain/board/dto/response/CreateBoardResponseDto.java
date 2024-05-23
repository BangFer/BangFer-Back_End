package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Board;
import lombok.Builder;
import java.time.LocalDateTime;
@Builder
public record CreateBoardResponseDto(
    Long id,
    Long writerId,
    String writerNickName,
    String boardTitle,
    String boardContent,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
    public static CreateBoardResponseDto from(Board board) {
        return CreateBoardResponseDto.builder()
                .id(board.getId())
                .writerId(board.getUser().getId())
                .writerNickName(board.getUser().getProfile().getNickname())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }
}

